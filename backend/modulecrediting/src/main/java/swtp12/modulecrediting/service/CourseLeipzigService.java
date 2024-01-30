package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.CourseLeipzigEditDTO;
import swtp12.modulecrediting.dto.CourseLeipzigRelationEditDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigCreateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;


@Service
public class CourseLeipzigService {
    @Autowired
    CourseLeipzigRepository courseLeipzigRepository;
    @Autowired
    ModuleLeipzigService moduleLeipzigService;

    @Autowired
    ApplicationRepository applicationRepository;

    public CourseLeipzig getCourseLeipzigByName(String name) {
        Optional<CourseLeipzig> courseLeipzig = courseLeipzigRepository.findByName(name);
        if(!courseLeipzig.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Leipzig not found with moduleName: " + name);
        
        return courseLeipzig.get();
    }

    public List<CourseLeipzig> getAllCoursesLeipzig() {
        return courseLeipzigRepository.findAll();
    }

    public String createCourseLeipzig(CourseLeipzigEditDTO courseLeipzigDTO) {
        if (courseLeipzigDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");
        String courseName = courseLeipzigDTO.getCourseName();
        Optional<CourseLeipzig> cL = courseLeipzigRepository.findByName(courseName);
        if (cL.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course with this name already exists: " + courseName);
        //TODO: reactivate
        CourseLeipzig courseLeipzig = new CourseLeipzig(courseName);
        courseLeipzigRepository.save(courseLeipzig);
        return courseName;
    }

    public String updateCourseLeipzig(String courseName, CourseLeipzigEditDTO courseLeipzigDTO) {
        if (courseLeipzigDTO == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        Optional<CourseLeipzig> courseLeipzigOptional = courseLeipzigRepository.findByName(courseName);

        if (!courseLeipzigOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Course with name " + courseName + " exists");

        CourseLeipzig courseLeipzig = courseLeipzigOptional.get();
        courseLeipzig.setName(courseLeipzigDTO.getCourseName());

        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }


    public String deleteCourseLeipzig(String name) {
        CourseLeipzig courseLeipzig = getCourseLeipzigByName(name);
        if (!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with name: " + name);

        courseLeipzig.setIsActive(false);

        if (!checkIfCourseIsUsedInApplications(courseLeipzig)) {
            courseLeipzigRepository.deleteById(courseLeipzig.getId());
            return "DELETED";
        }

        courseLeipzigRepository.save(courseLeipzig);
        return "DEACTIVATED";
    }

    private Boolean checkIfCourseIsUsedInApplications(CourseLeipzig courseLeipzig) {
        List<Application> applications = applicationRepository.findAll();

        if(applications.isEmpty()) return false;

        for(Application application : applications) {
            if(courseLeipzig.equals(application.getCourseLeipzig())) return true;
        }

        return false;
    }

    // TODO: edit course relations has ERRORS
    public Boolean editCourseRelations(String courseName, CourseLeipzigRelationEditDTO editCourseRelationsDTO) {
        if(editCourseRelationsDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if(courseName == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Course Name sent");

        CourseLeipzig courseLeipzig = getCourseLeipzigByName(courseName);
        List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();

        if(editCourseRelationsDTO.getModulesLeipzig() == null) {
            courseLeipzig.setModulesLeipzigCourse(modulesLeipzig);
            return true;
        }

        for(ModuleLeipzigCreateDTO ml : editCourseRelationsDTO.getModulesLeipzig()) {
            ModuleLeipzig moduleLeipzig = moduleLeipzigService.getModuleLeipzigByName(ml.getName());
            if(!moduleLeipzig.getCode().equals(ml.getCode()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Code dont match:" + moduleLeipzig.getName());

            modulesLeipzig.add(moduleLeipzig);
        }

        courseLeipzig.setModulesLeipzigCourse(modulesLeipzig);
        return true;
    }
}
