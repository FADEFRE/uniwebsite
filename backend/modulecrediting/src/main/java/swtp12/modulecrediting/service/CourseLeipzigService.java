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
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
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
        if (courseLeipzigDTO == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        Optional<CourseLeipzig> courseLeipzigOptional = courseLeipzigRepository.findByName(courseLeipzigDTO.getCourseName());
        if (courseLeipzigOptional.isPresent()) {
            CourseLeipzig courseLeipzig = courseLeipzigOptional.get();
            if(courseLeipzig.getIsActive()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig with this name already exists: " + courseLeipzig.getName());
            else {
                courseLeipzig.setIsActive(true);
                System.out.println("Reactivated Course Leipzig: " + courseLeipzig.getName());
            }
            courseLeipzigRepository.save(courseLeipzig);
            return courseLeipzig.getName();
        }

        // create new course leipzig
        CourseLeipzig courseLeipzig = new CourseLeipzig(courseLeipzigDTO.getCourseName());
        System.out.println("Created Course Leipzig: " + courseLeipzig.getName());
        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }

    public String updateCourseLeipzig(String courseName, CourseLeipzigEditDTO courseLeipzigDTO) {
        if (courseLeipzigDTO == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        CourseLeipzig courseLeipzig = getCourseLeipzigByName(courseName);

        if(!courseLeipzig.getIsActive())

        System.out.println(""); // idk why this is needed
        System.out.print("Update Course Leipzig: " + courseLeipzig.getName());
        courseLeipzig.setName(courseLeipzigDTO.getCourseName());
        System.out.print(" => " + courseLeipzig.getName());
        System.out.println("");

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
        List<Application> applications = applicationRepository.findAll();

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
