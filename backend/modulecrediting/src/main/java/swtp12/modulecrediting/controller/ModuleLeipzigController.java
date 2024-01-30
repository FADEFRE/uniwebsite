package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import swtp12.modulecrediting.dto.ModuleLeipzigCreateDTO;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ModuleLeipzigService;


@RestController
@RequestMapping("/api/modules-leipzig")
@CrossOrigin
public class ModuleLeipzigController {

    @Autowired
    private ModuleLeipzigService moduleLeipzigService;

    //GET-Requests
    @GetMapping
    @JsonView(Views.ModulesWithoutCourse.class)
    ResponseEntity<List<ModuleLeipzig>> getModulesLeipzig() {
        return ResponseEntity.ok(moduleLeipzigService.getModulesLeipzig());
    }
    
    //POST-Requests
    @PostMapping
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> createModuleLeipzig(@ModelAttribute ModuleLeipzigCreateDTO moduleLeipzigCreateDTO) {
        return ResponseEntity.ok(moduleLeipzigService.createModuleLeipzig(moduleLeipzigCreateDTO));
    }

    //PUT-Requests
    @PutMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> updateCourseLeipzig(@PathVariable String name, @ModelAttribute ModuleLeipzigCreateDTO moduleLeipzigCreateDTO) {
        return ResponseEntity.ok(moduleLeipzigService.updateModuleLeipzig(name, moduleLeipzigCreateDTO));
    }

    //DELETE-Requests
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> deleteModulesLeipzig(@PathVariable String name) {
        return ResponseEntity.ok(moduleLeipzigService.deleteModuleLeipzig(name));
    }
}
