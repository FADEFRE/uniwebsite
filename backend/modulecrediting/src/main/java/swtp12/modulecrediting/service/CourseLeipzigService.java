package swtp12.modulecrediting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.CourseLeipzigEditDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.OriginalApplication;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.OriginalApplicationRepository;



@Service
public class CourseLeipzigService {
    @Autowired
    CourseLeipzigRepository courseLeipzigRepository;

    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    OriginalApplicationRepository originalApplicationRepository;

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
        CourseLeipzig courseLeipzig = new CourseLeipzig(courseName, true);
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
        List<OriginalApplication> originalApplications = originalApplicationRepository.findAll();

        if(applications.isEmpty() && originalApplications.isEmpty()) return false;

        for(Application application : applications) {
            if(courseLeipzig.equals(application.getCourseLeipzig())) return true;
        }
        for(OriginalApplication originalApplication : originalApplications) {
            if(courseLeipzig.equals(originalApplication.getOriginalCourseLeipzig())) return true;
        }

        return false;
    }

    /*
    public Boolean editCourse(String courseName, CourseLeipzigRelationEditDTO editCourseDTO) {
        if (editCourseDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (editCourseDTO.getModuleId().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");
        String moduleName = editCourseDTO.getModuleId();
        String action = editCourseDTO.getAction();
        Optional<CourseLeipzig> cL = courseLeipzigRepository.findByName(courseName);
        Optional<ModuleLeipzig> mL = moduleLeipzigRepository.findByName(moduleName);
        if(!cL.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Leipzig not found with given name: " + courseName);
        if(!mL.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with given name: " + moduleName);

        CourseLeipzig courseLeipzig = cL.get();
        ModuleLeipzig moduleLeipzig = mL.get();

        switch (action) {
            case "delete":
                courseLeipzig.removeCourseToModulesLeipzig(moduleLeipzig);
                courseLeipzigRepository.save(courseLeipzig);
                moduleLeipzigRepository.save(moduleLeipzig);
                return true;
            case "add":
                if (courseLeipzig.getModulesLeipzigCourse().contains(moduleLeipzig)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, moduleName + " already exits in " + courseName);
                courseLeipzig.addCourseToModulesLeipzig(moduleLeipzig);
                courseLeipzigRepository.save(courseLeipzig);
                moduleLeipzigRepository.save(moduleLeipzig);
                return true;
            case "":
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No correct action given");
            default:
                break;
        }
        return false;
    }*/

}
