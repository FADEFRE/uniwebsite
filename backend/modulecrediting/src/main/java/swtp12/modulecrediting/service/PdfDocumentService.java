package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.PdfDocumentRepository;

@Service
public class PdfDocumentService {
    @Autowired
    private PdfDocumentRepository pdfDocumentRepository;


    public PdfDocument getPdfDocumentById(Long id) {
        return pdfDocumentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PDF document not found with id: " + id));
    }
}
