package swtp12.modulecrediting.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.service.CourseLeipzigService;

@RestController
@CrossOrigin
@RequestMapping("/courses-leipzig")
public class CourseLeipzigController {
    @Autowired
    CourseLeipzigService service;

    @GetMapping
    @JsonView(Views.coursesWithModules.class)
    ResponseEntity<List<CourseLeipzig>> getCourseLeipzigs() {
        return ResponseEntity.ok(service.getAllCoursesLeipzig());
    }
}
