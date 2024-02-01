package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import swtp12.modulecrediting.dto.CourseLeipzigEditDTO;
import swtp12.modulecrediting.dto.CourseLeipzigRelationEditDTO;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.CourseLeipzigService;

@RestController
@RequestMapping("/api/courses-leipzig")
public class CourseLeipzigController {
    @Autowired
    CourseLeipzigService courseLeipzigService;

    //GET-Requests
    @GetMapping
    @JsonView({Views.CoursesWithModules.class})
    ResponseEntity<List<CourseLeipzig>> getCoursesLeipzig() {
        return ResponseEntity.ok(courseLeipzigService.getAllCoursesLeipzig());
    }

    //POST-Requests
    @PostMapping
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> createCourseLeipzig(@ModelAttribute CourseLeipzigEditDTO courseLeipzigDTO) {
        return ResponseEntity.ok(courseLeipzigService.createCourseLeipzig(courseLeipzigDTO));
    }

    //PUT-Requests
    @PutMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> updateCourseLeipzig(@PathVariable String name, @ModelAttribute CourseLeipzigEditDTO courseLeipzigDTO) {
        return ResponseEntity.ok(courseLeipzigService.updateCourseLeipzig(name, courseLeipzigDTO));
    }

    @PutMapping("/{name}/edit") // deleting and adding modules
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> updateCourseLeipzig(@PathVariable String name, @ModelAttribute CourseLeipzigRelationEditDTO editCourseDTO) {
        return ResponseEntity.ok(courseLeipzigService.editCourseRelations(name, editCourseDTO));
    }

    //DELETE-Requests
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> deleteCourseLeipzig(@PathVariable String name) {
        return ResponseEntity.ok(courseLeipzigService.deleteCourseLeipzig(name));
    }

}