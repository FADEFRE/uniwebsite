package swtp12.modulecrediting.controller;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.service.ApplicationService;
import swtp12.modulecrediting.service.PdfDocumentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pdf-documents")
@CrossOrigin
public class PdfDocumentContoller {
    @Autowired
    private PdfDocumentService pdfDocumentService;
    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPdfDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfDocumentService.getPdfDocumentDataById(id));
    }
}
