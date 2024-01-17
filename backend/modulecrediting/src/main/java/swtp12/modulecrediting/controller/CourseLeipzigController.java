package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.CourseLeipzigService;

@RestController
@CrossOrigin
@RequestMapping("/api/courses-leipzig")
public class CourseLeipzigController {
    @Autowired
    CourseLeipzigService courseLeipzigService;

    @GetMapping
    @JsonView(Views.coursesWithModules.class)
    ResponseEntity<List<CourseLeipzig>> getCourseLeipzigs() {
        return ResponseEntity.ok(courseLeipzigService.getAllCoursesLeipzig());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CourseLeipzig> getSingleCourseLeipzig(@PathVariable String id) {
        return ResponseEntity.ok(courseLeipzigService.getCourseLeipzigById(id));
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteCourseLeipzig(@PathVariable String id) {
        if (courseLeipzigService.deleteCourseLeipzig(id)) {
            return ResponseEntity.ok("DELETED");
        }
        else return ResponseEntity.ok("DEACTIVATED");
    }

    @GetMapping("/{id}/state")
    public ResponseEntity<Boolean> getCourseLeipzigState(@PathVariable String id) {
        return ResponseEntity.ok(courseLeipzigService.getCourseLeipzigState(id));
    }
    
}
