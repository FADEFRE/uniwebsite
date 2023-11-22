package swtp12.modulecrediting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.service.ApplicationService;

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


}
