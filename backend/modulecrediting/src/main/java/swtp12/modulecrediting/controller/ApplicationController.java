package swtp12.modulecrediting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.ApplicationStatus;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ApplicationService;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateApplication(@PathVariable Long id, @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO) {
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO));
    }

    @PostMapping
    public ResponseEntity<Long> createApplication(@ModelAttribute ApplicationCreateDTO applicationCreateDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationCreateDTO));
    }

    @GetMapping
    @JsonView(Views.ApplicationOverview.class)
    public ResponseEntity<List<Application>> getAllCreditTransferApplications(@RequestParam(defaultValue = "10") int limit,
                                                                                        @RequestParam(required = false) ApplicationStatus status) {
        List<Application> allApplications = applicationService.getAllApplciations(limit, Optional.ofNullable(status));
        return new ResponseEntity<>(allApplications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.ApplicationLogin.class)
    public ResponseEntity<Application>  getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }


    @GetMapping("/student/{id}")
    @JsonView(Views.ApplicationStudent.class)
    public ResponseEntity<Application>  getApplicationStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> applicationExists(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.applicationExists(id));
    }
}