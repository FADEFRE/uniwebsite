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

    @GetMapping("/{id}")
    public ResponseEntity<ModuleLeipzig> getSingleModulesLeipzig(@PathVariable String id) {
        return ResponseEntity.ok(moduleLeipzigService.getModuleLeipzigById(id));
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteModulesLeipzig(@PathVariable String id) {
        if (moduleLeipzigService.deleteModulesLeipzig(id)) {
            return ResponseEntity.ok("DELETED");
        }
        else return ResponseEntity.ok("DEACTIVATED");
    }

    @GetMapping("/{id}/state")
    public ResponseEntity<Boolean> getModuleLeipzigState(@PathVariable String id) {
        return ResponseEntity.ok(moduleLeipzigService.getModuleLeipzigState(id));
    }
    


}
