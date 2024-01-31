package swtp12.modulecrediting.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import swtp12.modulecrediting.service.GeneratedPdfService;
import swtp12.modulecrediting.service.PdfDocumentService;

@RestController
@RequestMapping("/file/pdf-documents")
public class PdfDocumentContoller {
    @Autowired
    private PdfDocumentService pdfDocumentService;
    
    @Autowired
    private GeneratedPdfService generatedPdfService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPdfDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfDocumentService.getPdfDocumentDataById(id));
    }
    
    @GetMapping("/application/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String id) throws DocumentException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Antrag.pdf");

        byte[] pdfBytes = generatedPdfService.generatePdfFromHtml(id);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }
}
