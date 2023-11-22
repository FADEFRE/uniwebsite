package swtp12.modulecrediting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.service.ApplicationService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Long> createApplication(@ModelAttribute ApplicationCreateDTO applicationCreateDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationCreateDTO));
    }

    @GetMapping
    public ResponseEntity<List<Application>>getAllApplications(@RequestParam(defaultValue = "10") int limit){
        List<Application> applicationRequests = applicationService.getAllApplciations(limit);
        return new ResponseEntity<>(applicationRequests, HttpStatus.OK);
    }


}
