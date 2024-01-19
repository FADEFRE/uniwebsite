package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import swtp12.modulecrediting.dto.CourseLeipzigCreateDTO;
import swtp12.modulecrediting.dto.EditCourseDTO;
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


    @GetMapping("/{name}")
    public ResponseEntity<CourseLeipzig> getSingleCourseLeipzig(@PathVariable String name) {
        return ResponseEntity.ok(courseLeipzigService.getCourseLeipzigByName(name));
    }


    @DeleteMapping("/{name}/delete")
    public ResponseEntity<String> deleteCourseLeipzig(@PathVariable String name) {
        if (courseLeipzigService.deleteCourseLeipzig(name)) return ResponseEntity.ok("DELETED");
        else return ResponseEntity.ok("DEACTIVATED");
    }

    @GetMapping("/{name}/state")
    public ResponseEntity<Boolean> getCourseLeipzigState(@PathVariable String name) {
        return ResponseEntity.ok(courseLeipzigService.getCourseLeipzigState(name));
    }
    
    @PutMapping("/{name}/edit")
    public ResponseEntity<Boolean> editCourse(@PathVariable String name, @ModelAttribute EditCourseDTO editCourseDTO) {
        return ResponseEntity.ok(courseLeipzigService.editCourse(name, editCourseDTO));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCourseLeipzig(@ModelAttribute CourseLeipzigCreateDTO courseLeipzigCreateDTO) {
        return ResponseEntity.ok(courseLeipzigService.createApplication(courseLeipzigCreateDTO));
    }

}