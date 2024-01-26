package swtp12.modulecrediting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.CourseLeipzigCreateDTO;
import swtp12.modulecrediting.dto.EditCourseDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;



@Service
public class CourseLeipzigService {
    @Autowired
    CourseLeipzigRepository courseLeipzigRepository;
    
    @Autowired
    private ModulesConnectionRepository modulesConnectionRepository;

    @Autowired
    private ModuleLeipzigRepository moduleLeipzigRepository;

    public CourseLeipzig getCourseLeipzigByName(String name) {
        Optional<CourseLeipzig> courseLeipzig = courseLeipzigRepository.findById(name);
        if(!courseLeipzig.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Leipzig not found with moduleName: " + name);
        
        return courseLeipzig.get();
    }

    public List<CourseLeipzig> getAllCoursesLeipzig() {
        return courseLeipzigRepository.findAll();
    }

    public Boolean deleteCourseLeipzig(String name) {
        CourseLeipzig courseLeipzig = getCourseLeipzigByName(name);
        if (!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with name: " + name);
        courseLeipzig.setIsActive(false);
        if (checkIfDeletionIsPossible(courseLeipzig)) {
            courseLeipzigRepository.deleteById(name);
            return true;
        }
        else return false;
    }

    private Boolean checkIfDeletionIsPossible(CourseLeipzig courseLeipzig) {
        List<ModulesConnection> allModulesConnections = modulesConnectionRepository.findAll();
        if (allModulesConnections.isEmpty()) return true;
        Boolean check = false;
        for (ModulesConnection modulesConnection : allModulesConnections) {
            Application application = modulesConnection.getApplication();
            CourseLeipzig applicationCourseLeipzig = application.getCourseLeipzig();
            if (!applicationCourseLeipzig.equals(courseLeipzig)) { check = true; }
            else check = false;
        }
        return check;
    }

    public Boolean editCourse(String courseName, EditCourseDTO editCourseDTO) {
        if (editCourseDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (editCourseDTO.getModuleId().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");
        String moduleName = editCourseDTO.getModuleId();
        String action = editCourseDTO.getAction();
        Optional<CourseLeipzig> cL = courseLeipzigRepository.findById(courseName);
        Optional<ModuleLeipzig> mL = moduleLeipzigRepository.findById(moduleName);
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
    }

    public String createCourseLeipzig(CourseLeipzigCreateDTO courseLeipzigCreateDTO) {
        if (courseLeipzigCreateDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigCreateDTO.getCourseName() == null || courseLeipzigCreateDTO.getCourseName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");
        String courseName = courseLeipzigCreateDTO.getCourseName();
        Optional<CourseLeipzig> cL = courseLeipzigRepository.findById(courseName);
        if (cL.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course with this name already exists: " + courseName);

        CourseLeipzig courseLeipzig = new CourseLeipzig(courseName, true);
        courseLeipzigRepository.save(courseLeipzig);
        return courseName;
    }
}
