package swtp12.modulecrediting.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.dto.EnumStatusChange;
import swtp12.modulecrediting.dto.StudentApplicationDTO;
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

    //GET-Requests
    @GetMapping
    @JsonView(Views.ApplicationOverview.class)
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<List<Application>> getApplicationsOverview() {
        return ResponseEntity.ok(applicationService.getAllApplciations());
    }

    @GetMapping("/{id}")
    @JsonView(Views.ApplicationLogin.class)
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<Application>  getApplicationById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    //TODO: FIX THAT ONLY REQUIRED FIELDS ARE PASSED
    @GetMapping("/student/{id}")
    @JsonView(Views.ApplicationStudent.class)
    public ResponseEntity<StudentApplicationDTO>  getApplicationStudentById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getStudentApplicationById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> applicationExists(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.applicationExists(id));
    }

    @GetMapping("/{id}/update-status-allowed")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<EnumStatusChange> updateApplicationStatusAllowed(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.updateApplicationStatusAllowed(id));
    }


    //POST-Requests
    @PostMapping
    public ResponseEntity<String> createApplication(@ModelAttribute ApplicationCreateDTO applicationCreateDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationCreateDTO));
    }


    //PUT-Requests
    @PutMapping("/{id}/update-status")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<EnumApplicationStatus> updateApplicationStatus(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id));
    }

    @PutMapping("/standard/{id}")
    public ResponseEntity<String> updateApplicationStandard(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "standard"));
    }

    @PutMapping("/study-office/{id}")
    @PreAuthorize("hasRole('ROLE_STUDY')")
    public ResponseEntity<String> updateApplicationStudyOffice(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "study-office"));
    }

    @PutMapping("/chairman/{id}")
    @PreAuthorize("hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> updateApplicationChairman(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "chairman"));
    }

}