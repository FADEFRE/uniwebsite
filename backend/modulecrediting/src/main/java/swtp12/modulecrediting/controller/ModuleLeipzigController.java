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

    @GetMapping
    @JsonView(Views.modulesWithoutCourse.class)
    ResponseEntity<List<ModuleLeipzig>> getAllModulesLeipzig() {
        return ResponseEntity.ok(moduleLeipzigService.getAllModulesLeipzig());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ModuleLeipzig> getSingleModulesLeipzig(@PathVariable String name) {
        return ResponseEntity.ok(moduleLeipzigService.getModuleLeipzigByName(name));
    }


    @DeleteMapping("/{name}/delete")
    public ResponseEntity<String> deleteModulesLeipzig(@PathVariable String name) {
        if (moduleLeipzigService.deleteModulesLeipzig(name)) {
            return ResponseEntity.ok("DELETED");
        }
        else return ResponseEntity.ok("DEACTIVATED");
    }

    @GetMapping("/{name}/state")
    public ResponseEntity<Boolean> getModuleLeipzigState(@PathVariable String name) {
        return ResponseEntity.ok(moduleLeipzigService.getModuleLeipzigState(name));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createModuleLeipzig(@ModelAttribute ModuleLeipzigCreateDTO moduleLeipzigCreateDTO) {
        return ResponseEntity.ok(moduleLeipzigService.createModuleLeipzig(moduleLeipzigCreateDTO));
    }
    
}
