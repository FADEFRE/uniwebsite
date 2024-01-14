package swtp12.modulecrediting.controller;

import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ApplicationService;
import swtp12.modulecrediting.service.GeneratedPdfService;


@RestController
@CrossOrigin
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private GeneratedPdfService generatedPdfService;

    @PutMapping("/{id}")
    public ResponseEntity<String> updateApplication(@PathVariable String id,
                                                    @Valid @ModelAttribute ApplicationUpdateDTO applicationUpdateDTO,
                                                    BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed: " + result.getAllErrors());
        }

        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO));
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

    @GetMapping("/pdf-data/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String id) throws DocumentException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDispositionFormData("att", "Antrag.pdf");
        byte[] pdfBytes = generatedPdfService.generatePdfDataDocument(id);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);

    }
}