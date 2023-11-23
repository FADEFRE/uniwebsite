package swtp12.modulecrediting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import swtp12.modulecrediting.service.PdfDocumentService;

@RestController
@RequestMapping("/pdf-documents")
@CrossOrigin
public class PdfDocumentContoller {

    @Autowired
    private PdfDocumentService pdfDocumentService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPdfDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfDocumentService.getPdfDocumentDataById(id));
    }
}
