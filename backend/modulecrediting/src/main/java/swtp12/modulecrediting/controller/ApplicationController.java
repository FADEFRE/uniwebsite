package swtp12.modulecrediting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationDTO;
import swtp12.modulecrediting.dto.ApplicationStudentDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.repository.projection.ApplicationProjection;
import swtp12.modulecrediting.service.ApplicationService;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Long> createApplication(@ModelAttribute ApplicationCreateDTO applicationCreateDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationCreateDTO));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationProjection>> getAllCreditTransferApplications(@RequestParam(defaultValue = "10") int limit,
                                                                                        @RequestParam(required = false) Application.ApplicationStatus status) {

        List<ApplicationProjection> allApplications = applicationService.getAllApplciations(limit, Optional.ofNullable(status));
        return new ResponseEntity<>(allApplications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO>  getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<ApplicationStudentDTO>  getApplicationStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationStudentById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> applicationExists(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.applicationExists(id));
    }
}
