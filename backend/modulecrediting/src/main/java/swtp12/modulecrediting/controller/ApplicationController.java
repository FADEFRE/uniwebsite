package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.Valid;
import swtp12.modulecrediting.dto.ApplicationDTO;
import swtp12.modulecrediting.dto.EnumStatusChangeAllowed;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ApplicationService;


@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    //GET-Requests
    @GetMapping
    @JsonView(Views.ApplicationLoginOverview.class)
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<List<Application>> getApplicationsOverview() {
        return ResponseEntity.ok(applicationService.getAllApplciations());
    }

    @GetMapping("/{id}")
    @JsonView(Views.ApplicationLogin.class)
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<Application>  getApplicationLoginById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/student/{id}")
    @JsonView(Views.ApplicationStudent.class)
    public ResponseEntity<Application>  getApplicationStudentById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getApplicationStudentById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> applicationExists(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.applicationExists(id));
    }


    @GetMapping("/{id}/update-status-allowed")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<EnumStatusChangeAllowed> updateApplicationStatusAllowed(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.updateApplicationStatusAllowed(id));
    }

    //POST-Requests
    @PostMapping
    public ResponseEntity<String> createApplication(@ModelAttribute ApplicationDTO applicationDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationDTO));
    }

    //PUT-Requests
    @PutMapping("/student/{id}")
    public ResponseEntity<String> updateApplicationAfterFormalRejection(@PathVariable String id,
                                                                        @Valid @ModelAttribute ApplicationDTO applicationDTO,
                                                                        BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplicationAfterFormalRejection(id, applicationDTO));
    }

    @PutMapping("/{id}/update-status")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    public ResponseEntity<EnumApplicationStatus> updateApplicationStatus(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id));
    }

    @PutMapping("/study-office/{id}")
    @PreAuthorize("hasRole('ROLE_STUDY')")
    public ResponseEntity<String> updateApplicationStudyOffice(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "study-office"));
    }

    @PutMapping("/chairman/{id}")
    @PreAuthorize("hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> updateApplicationChairman(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO, "chairman"));
    }
}