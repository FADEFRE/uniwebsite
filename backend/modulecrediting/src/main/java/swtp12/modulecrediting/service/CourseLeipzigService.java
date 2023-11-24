package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.CourseLeipzigWithoutModulesDTO;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig not found with moduleName: " + name);
        }
    }

    public CourseLeipzigWithoutModulesDTO mapToCourseLeipzigWithoutModulesDTO(CourseLeipzig courseLeipzig) {
        return new CourseLeipzigWithoutModulesDTO(courseLeipzig.getId(),courseLeipzig.getName());
    }
}
