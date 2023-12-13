package swtp12.modulecrediting.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ApplicationService;


@RestController
@CrossOrigin
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PutMapping("/{id}")
    public ResponseEntity<String> updateApplication(@PathVariable String id,@ModelAttribute ApplicationUpdateDTO applicationUpdateDTO) {
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationUpdateDTO));
    }

    @PostMapping
    public ResponseEntity<String> createApplication(@ModelAttribute ApplicationCreateDTO applicationCreateDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationCreateDTO));
    }

    @GetMapping
    @JsonView(Views.ApplicationOverview.class)
    public ResponseEntity<List<Application>> getAllCreditTransferApplications(
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(required = false) EnumApplicationStatus status) {
            List<Application> allApplications = applicationService.getAllApplciations(limit, Optional.ofNullable(status));
            return new ResponseEntity<>(allApplications, HttpStatus.OK);
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
        byte[] pdfBytes = applicationService.generatePdfDataDocument(id);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);

    }
}