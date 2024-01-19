package swtp12.modulecrediting.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ApplicationService;


@RestController
@CrossOrigin
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    //TODO: add auth study-office put request
    @PutMapping("/study-office/{id}")
    public ResponseEntity<String> updateApplicationStudyOffice(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "study-office"));
    }
    //TODO: add auth chairman put request
    @PutMapping("/chairman/{id}")
    public ResponseEntity<String> updateApplicationChairman(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "chairman"));
    }
    //TODO: add auth standard put request? if needed.
    @PutMapping("/standard/{id}")
    public ResponseEntity<String> updateApplicationStandard(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "standard"));
    }



    @GetMapping("/{id}/update-status-allowed")
    public ResponseEntity<Boolean> updateApplicationStatusAllowed(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.updateApplicationStatusAllowed(id));
    }
    @PutMapping("/{id}/update-status")
    public ResponseEntity<EnumApplicationStatus> updateApplicationStatus(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id));
    }

    @PostMapping
    public ResponseEntity<String> createApplication(@ModelAttribute ApplicationCreateDTO applicationCreateDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationCreateDTO));
    }

    @GetMapping
    @JsonView(Views.ApplicationOverview.class)
    public ResponseEntity<List<Application>> get() {
        return ResponseEntity.ok(applicationService.getAllApplciations());
    }

    @GetMapping("/{id}")
    @JsonView(Views.ApplicationLogin.class)
    public ResponseEntity<Application>  getApplicationById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }


    @GetMapping("/student/{id}")
    @JsonView(Views.ApplicationStudent.class)
    public ResponseEntity<Application>  getApplicationStudentById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> applicationExists(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.applicationExists(id));
    }

    
}