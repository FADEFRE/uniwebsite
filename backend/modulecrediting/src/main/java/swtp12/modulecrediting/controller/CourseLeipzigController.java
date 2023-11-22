package swtp12.modulecrediting.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

@RestController
@CrossOrigin
public class CourseLeipzigController {

    @Autowired
    ModuleLeipzigRepository moduleLeipzigRepository;

    @Autowired
    CourseLeipzigRepository courseLeipzigRepository;

    @GetMapping("/course-leipzig-test")
    List<CourseLeipzig> getCourseLeipzigs() {
        return courseLeipzigRepository.findAll();
    }
    /*
    @GetMapping("/course-leipzig/{id}")
    public ResponseEntity<CourseLeipzig> getCourseLeipzigById(@PathVariable("id") Long id) {
        Optional<CourseLeipzig> courseLeipzig = courseLeipzigRepository.findById(id);
        if (courseLeipzig.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(courseLeipzig.get(), HttpStatus.OK);
    }

    @PostMapping("/course-leipzig")
    public ResponseEntity<CourseLeipzig> createCourseLeipzig(@RequestBody CourseLeipzig courseLeipzig) {
        if (courseLeipzig.getModulesLeipzigCourse().size() != 0) {
            courseLeipzig.getModulesLeipzigCourse().stream().map((module -> {
                                        if (module.getId() == null) {return moduleLeipzigRepository.save(module);}
                                        return module;
                                    })).collect(Collectors.toList());
        }
        CourseLeipzig resCourseLeipzig = courseLeipzigRepository.save(new CourseLeipzig(courseLeipzig.getName()));

        return new ResponseEntity<>(resCourseLeipzig, HttpStatus.CREATED);
    }

    @DeleteMapping("/course-Leipzig/{id}")
    public ResponseEntity<HttpStatus> deleteCourseLeipzigById(@PathVariable("id") Long id) {
        courseLeipzigRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    */
}
