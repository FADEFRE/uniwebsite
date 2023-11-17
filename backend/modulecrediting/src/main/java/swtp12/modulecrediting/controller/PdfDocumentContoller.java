package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.PdfDocumentRepository;

@RestController
@RequestMapping("/pdf-documents")
public class PdfDocumentContoller {

    @Autowired
    private PdfDocumentRepository pdfDocumentRepository;

    @GetMapping
    List<PdfDocument> getPdfDocuments() {
        return pdfDocumentRepository.findAll();
    }

}
