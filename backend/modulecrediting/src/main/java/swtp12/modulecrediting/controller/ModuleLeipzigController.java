
package swtp12.modulecrediting.controller;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

@RestController
@RequestMapping("/modules-leipzig")
@CrossOrigin
public class ModuleLeipzigController {
    @Autowired
    private ModuleLeipzigRepository modulLeipzigRepository;

    @GetMapping
    @JsonView(Views.modulesWithoutCourse.class)
    ResponseEntity<List<ModuleLeipzig>> getModulesLeipzig() {
        return ResponseEntity.ok(modulLeipzigRepository.findAll());
    }
}
