package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.PdfDocumentRepository;
import swtp12.modulecrediting.service.PdfDocumentService;

@RestController
@RequestMapping("/pdf-documents")
public class PdfDocumentContoller {

    @Autowired
    private PdfDocumentService pdfDocumentService;


    @GetMapping(value = "/{id}")
    public ResponseEntity<PdfDocument> getPdfDocumentById(@PathVariable Long id) {
        PdfDocument pdfDocument = pdfDocumentService.getPdfDocumentById(id);
        //if(pdfDocument == null) ResponseEntity.notFound().build();
        return ResponseEntity.ok(pdfDocumentService.getPdfDocumentById(id));

    }
}
