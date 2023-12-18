package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.Views;
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
