package swtp12.modulecrediting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;



@Service
public class CourseLeipzigService {
    @Autowired
    CourseLeipzigRepository courseLeipzigRepository;
    
    @Autowired
    private ModulesConnectionRepository modulesConnectionRepository;

    public CourseLeipzig getCourseLeipzigByName(String name) {
        Optional<CourseLeipzig> courseLeipzig = courseLeipzigRepository.findByName(name);
        if(courseLeipzig.isPresent()) {
            return courseLeipzig.get();
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig not found with moduleName: " + name);
        }
    }

    public List<CourseLeipzig> getAllCoursesLeipzig() {
        return courseLeipzigRepository.findAll();
    }

    public CourseLeipzig getCourseLeipzigById(String id) {
        Optional<CourseLeipzig> courseLeipzig = courseLeipzigRepository.findById(id);
        if(courseLeipzig.isPresent()) 
            return courseLeipzig.get();

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig not found with given id: " + id);
    }

    public Boolean deleteCourseLeipzig(String id) {
        CourseLeipzig courseLeipzig = getCourseLeipzigById(id);
        if (!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with id: " + id);
        courseLeipzig.setIsActive(false);
        if (checkIfDeletionIsPossible(courseLeipzig)) {
            courseLeipzigRepository.deleteById(id);
            return true;
        }
        else return false;
    }

    private Boolean checkIfDeletionIsPossible(CourseLeipzig courseLeipzig) {
        List<ModulesConnection> allModulesConnections = modulesConnectionRepository.findAll();
        if (allModulesConnections.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are no connections in the database");
        Boolean check = false;
        for (ModulesConnection modulesConnection : allModulesConnections) {
            Application application = modulesConnection.getApplication();
            CourseLeipzig applicationCourseLeipzig = application.getCourseLeipzig();
            if (!applicationCourseLeipzig.equals(courseLeipzig)) { check = true; }
            else check = false;
        }
        return check;
    }

    public Boolean getCourseLeipzigState(String id) {
        CourseLeipzig courseLeipzig = getCourseLeipzigById(id);
        return courseLeipzig.getIsActive();
    }
}
