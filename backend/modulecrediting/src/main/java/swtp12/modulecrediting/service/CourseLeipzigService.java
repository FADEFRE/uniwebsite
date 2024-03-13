package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import swtp12.modulecrediting.dto.CourseLeipzigDTO;
import swtp12.modulecrediting.dto.CourseLeipzigRelationEditDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.util.LogUtil;


@Service
public class CourseLeipzigService {
    CourseLeipzigRepository courseLeipzigRepository;
    ModuleLeipzigService moduleLeipzigService;
    ApplicationService applicationService;

    @Autowired
    public CourseLeipzigService(CourseLeipzigRepository courseLeipzigRepository, @Lazy ModuleLeipzigService moduleLeipzigService, @Lazy ApplicationService applicationService) {
        this.courseLeipzigRepository = courseLeipzigRepository;
        this.moduleLeipzigService = moduleLeipzigService;
        this.applicationService = applicationService;
    }
    public CourseLeipzigService() { } // This cannot!! be a lombok @NoArgsConstructor due to the way @Autowired works in Springboot


    public CourseLeipzig getCourseLeipzigByName(String name) {
        CourseLeipzig courseLeipzig = courseLeipzigRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Leipzig not found with given name: " + name));
        return courseLeipzig;
    }

    public String getCourseLeipzigNameById(Long id) {
        CourseLeipzig courseLeipzig = courseLeipzigRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Leipzig not found with given id: " + id));
        return courseLeipzig.getName();
    }

    public List<CourseLeipzig> getAllCoursesLeipzig() {
        return courseLeipzigRepository.findAll();
    }

    public CourseLeipzig findOrCreateNewCourseLeipzig(String name) {
        CourseLeipzig courseLeipzig = courseLeipzigRepository.findByName(name).orElseGet(() -> {
            return courseLeipzigRepository.save(new CourseLeipzig(name));
        });
        LogUtil.printCourseLog(LogUtil.CourseType.CREATED, name, null);
        courseLeipzig.setIsActive(true);
        return courseLeipzig;
    }

    public CourseLeipzig saveCourseLeipzigToDatabase(CourseLeipzig courseLeipzig) {
        return courseLeipzigRepository.save(courseLeipzig);
    }

    public String createCourseLeipzig(CourseLeipzigDTO courseLeipzigDTO) {
        if (courseLeipzigDTO == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        Optional<CourseLeipzig> courseLeipzigOptional = courseLeipzigRepository.findByName(courseLeipzigDTO.getCourseName());
        if (courseLeipzigOptional.isPresent()) {
            CourseLeipzig courseLeipzig = courseLeipzigOptional.get();
            if(courseLeipzig.getIsActive()) 
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The Course already exists:" + courseLeipzigDTO.getCourseName() );

            courseLeipzig.setIsActive(true);
            LogUtil.printCourseLog(LogUtil.CourseType.REACTIVATED, courseLeipzig.getName(), null);
            courseLeipzigRepository.save(courseLeipzig);
            return courseLeipzig.getName();
        }

        CourseLeipzig courseLeipzig = new CourseLeipzig(courseLeipzigDTO.getCourseName());
        LogUtil.printCourseLog(LogUtil.CourseType.CREATED, courseLeipzig.getName(), null);
        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }

    public String updateCourseLeipzig(String courseName, CourseLeipzigDTO courseLeipzigDTO) {
        if (courseLeipzigDTO == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        CourseLeipzig courseLeipzig = getCourseLeipzigByName(courseName);

        if(!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is inactive");

        Optional<CourseLeipzig> possibleConflictCourse = courseLeipzigRepository.findByName(courseLeipzigDTO.getCourseName());
        if (possibleConflictCourse.isPresent() && !courseName.equals(courseLeipzigDTO.getCourseName()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course with this name already exists");

        courseLeipzig.setName(courseLeipzigDTO.getCourseName());
        LogUtil.printCourseLog(LogUtil.CourseType.RENAMED, courseName, courseLeipzig.getName());
        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }


    public String deleteCourseLeipzig(String name) {
        CourseLeipzig courseLeipzig = getCourseLeipzigByName(name);
        if (!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with name: " + name);

        courseLeipzig.setIsActive(false);

        if (!checkIfCourseIsUsedInApplications(courseLeipzig)) {
            LogUtil.printCourseLog(LogUtil.CourseType.DELETED, courseLeipzig.getName(), null);
            courseLeipzigRepository.deleteById(courseLeipzig.getId());
            return "DELETED";
        }

        LogUtil.printCourseLog(LogUtil.CourseType.DEACTIVATED, courseLeipzig.getName(), null);
        courseLeipzigRepository.save(courseLeipzig);
        return "DEACTIVATED";
    }

    private Boolean checkIfCourseIsUsedInApplications(CourseLeipzig courseLeipzig) {
        List<Application> applications = applicationService.getAllApplciations();

        if(applications.isEmpty()) return false;

        for(Application application : applications) {
            if(courseLeipzig.equals(application.getCourseLeipzig())) return true;
        }

        return false;
    }

    public String editCourseRelations(String courseName, CourseLeipzigRelationEditDTO editCourseRelationsDTO) {
        if(editCourseRelationsDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if(courseName == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Course Name sent");

        CourseLeipzig courseLeipzig = getCourseLeipzigByName(courseName);
        if(!courseLeipzig.getIsActive()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig with this name is deactivated: " + courseLeipzig.getName());

        List<ModuleLeipzig> modulesLeipzigToDelete = new ArrayList<>(courseLeipzig.getModulesLeipzigCourse());
        List<ModuleLeipzig> modulesLeipzigToAdd = new ArrayList<>();

        // add new modules
        if(editCourseRelationsDTO.getModulesLeipzig() != null) {
            for(ModuleLeipzigDTO mLDTO : editCourseRelationsDTO.getModulesLeipzig()) {
                if(mLDTO.getName() == null)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Name sent");
                if(mLDTO.getCode() == null)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Code sent");

                ModuleLeipzig moduleLeipzig = moduleLeipzigService.getModuleLeipzigByName(mLDTO.getName());

                if(!moduleLeipzig.getCode().equals(mLDTO.getCode()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Code doesn't match: " + mLDTO.getName() + " <-> " + mLDTO.getCode());

                modulesLeipzigToDelete = removeModulesFromList(modulesLeipzigToDelete, moduleLeipzig);
                modulesLeipzigToAdd.add(moduleLeipzig);
            }
        }

        for (ModuleLeipzig moduleLeipzig : modulesLeipzigToDelete) {
            LogUtil.printCourseLog(LogUtil.CourseType.REMOVED, courseLeipzig.getName(), moduleLeipzig.getName());
            courseLeipzig.removeModuleLeipzig(moduleLeipzig);
        }
        
        for (ModuleLeipzig moduleLeipzig : courseLeipzig.getModulesLeipzigCourse()) {
            modulesLeipzigToAdd = removeModulesFromList(modulesLeipzigToAdd, moduleLeipzig);
        }

        for (ModuleLeipzig moduleLeipzig : modulesLeipzigToAdd) {
            LogUtil.printCourseLog(LogUtil.CourseType.ADDED, courseLeipzig.getName(), moduleLeipzig.getName());
            courseLeipzig.addModulesLeipzig(moduleLeipzig);
        }

        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }

    private List<ModuleLeipzig> removeModulesFromList(List<ModuleLeipzig> moduleList, ModuleLeipzig moduleLeipzig) {
        for (int i = 0; i < moduleList.size(); i++) {
            ModuleLeipzig mL = moduleList.get(i);
            if (moduleLeipzig.getName().equals(mL.getName())) {
                moduleList.remove(i);
            }
        }
        return moduleList;
    }
}
