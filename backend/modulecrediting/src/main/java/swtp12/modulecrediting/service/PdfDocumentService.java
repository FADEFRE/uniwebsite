package swtp12.modulecrediting.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.PdfDocumentRepository;


@Service
public class PdfDocumentService {

    PdfDocumentRepository pdfDocumentRepository;

    public PdfDocumentService(PdfDocumentRepository pdfDocumentRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
    }

    public PdfDocument createOrGetPdfDocument(MultipartFile pdfData, Long pdfId) {

        if (pdfData == null) {
            if (pdfId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No external module id given");
            }
            else return getPdfDocumentById(pdfId);
        }
        return createPdfDocument(pdfData);
    }

    public PdfDocument createPdfDocument(MultipartFile pdfData) {
        if (pdfData == null || pdfData.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No PDF file provided or file is empty");

        try {
            return new PdfDocument(pdfData.getOriginalFilename(), pdfData.getBytes());
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading PDF file", ioException);
        }
    }


    public byte[] getPdfDocumentDataById(Long id) {
        PdfDocument pdfDocument = getPdfDocumentById(id);
        return pdfDocument.getPdfData();
    }

    public String getPdfDocumentNameById(Long id) {
        PdfDocument pdfDocument = getPdfDocumentById(id);
        return pdfDocument.getName();
    }

    public PdfDocument getPdfDocumentById(Long id) {
        Optional<PdfDocument> pdfDocumentOptional = pdfDocumentRepository.findById(id);
        if (pdfDocumentOptional.isPresent()) return pdfDocumentOptional.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PDF document not found with id: " + id);
    }
}
