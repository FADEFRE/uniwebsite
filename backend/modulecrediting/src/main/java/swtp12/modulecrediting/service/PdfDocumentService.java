package swtp12.modulecrediting.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.PdfDocumentRepository;



@Service
public class PdfDocumentService {
    @Autowired
    private PdfDocumentRepository pdfDocumentRepository;


    public PdfDocument createPdfDocument(MultipartFile pdf) {
        if (pdf == null || pdf.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No PDF file provided or file is empty");
        }
        
        try {
            return new PdfDocument(pdf.getOriginalFilename(), pdf.getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            // Throw a ResponseStatusException with the appropriate HTTP status code
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading PDF file", ioException);
        }
    }


    public byte[] getPdfDocumentDataById(Long id) {
        Optional<PdfDocument> pdfDocumentOptional = pdfDocumentRepository.findById(id);
        if (pdfDocumentOptional.isPresent()) {
            PdfDocument pdfDocument = pdfDocumentOptional.get();
            return pdfDocument.getPdfData();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PDF document not found with id: " + id);
        }
    }
}
