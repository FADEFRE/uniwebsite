package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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


@Service
public class CourseLeipzigService {
    @Autowired
    CourseLeipzigRepository courseLeipzigRepository;
    @Autowired
    ModuleLeipzigService moduleLeipzigService;
    @Autowired
    ApplicationService applicationService;

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
            courseLeipzigRepository.save(courseLeipzig);
            return courseLeipzig.getName();
        }

        CourseLeipzig courseLeipzig = new CourseLeipzig(courseLeipzigDTO.getCourseName());
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
        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }


    public String deleteCourseLeipzig(String name) {
        CourseLeipzig courseLeipzig = getCourseLeipzigByName(name);
        if (!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with name: " + name);

        courseLeipzig.setIsActive(false);

        if (!checkIfCourseIsUsedInApplications(courseLeipzig)) {
            System.out.println("Delete Course Leipzig: " + courseLeipzig.getName());
            courseLeipzigRepository.deleteById(courseLeipzig.getId());
            return "DELETED";
        }

        System.out.println("Deactivate Course Leipzig: " + courseLeipzig.getName());
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

        List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();

        // remove all modules from course leipzig
        courseLeipzig.removeModulesLeipzig();
        System.out.println("Remove all Modules from Course: " + courseLeipzig.getName());

        // add new modules
        if(editCourseRelationsDTO.getModulesLeipzig() != null) {
            for(ModuleLeipzigDTO ml : editCourseRelationsDTO.getModulesLeipzig()) {
                if(ml.getName() == null)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Name sent");
                if(ml.getCode() == null)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Code sent");


                ModuleLeipzig moduleLeipzig = moduleLeipzigService.getModuleLeipzigByName(ml.getName());

                if(!moduleLeipzig.getCode().equals(ml.getCode()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Code doesn't match: " + ml.getName() + " <-> " + ml.getCode());

                System.out.println("Added Module to Course: " + moduleLeipzig.getName() + " => " + courseLeipzig.getName());
                modulesLeipzig.add(moduleLeipzig);
            }
        }

        courseLeipzig.setModulesLeipzigCourse(modulesLeipzig);
        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }
}
