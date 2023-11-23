package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CourseLeipzigService {
    @Autowired
    CourseLeipzigRepository courseLeipzigRepository;

    public CourseLeipzig getCourseLeipzigByName(String name) {
        Optional<CourseLeipzig> courseLeipzig = courseLeipzigRepository.findByName(name);
        if(courseLeipzig.isPresent()) {
            return courseLeipzig.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with moduleName: " + name);
        }
    }
}
