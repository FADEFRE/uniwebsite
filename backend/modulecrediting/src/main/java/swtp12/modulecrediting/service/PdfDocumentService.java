package swtp12.modulecrediting.service;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.PdfDocumentRepository;

/**
 * This is a {@code Service} for {@link PdfDocument} and provides {@code create} and  {@code get} methods.
 * @author Frederik Kluge
 * @author Luca Kippe
 * @see #getOrCreatePdfDocument
 * @see #createPdfDocument
 * @see #getPdfDocumentDataById
 * @see #getPdfDocumentNameById
 * @see #getPdfDocumentById
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Springboot Service</a>
 */
@Service
public class PdfDocumentService {

    PdfDocumentRepository pdfDocumentRepository;

    public PdfDocumentService(PdfDocumentRepository pdfDocumentRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
    }

    /**
     * This method either gets the {@link PdfDocument} with the given {@code pdfId} or creates a new one.
     * @param pdfData {@code MultipartFile} of the {@link PdfDocument} you want to create
     * @param pdfId of the {@link PdfDocument} you want to get
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if both {@code pdfData} and {@code pdfId} are {@code null}
     * @return either the created {@link PdfDocument} or the already existing one with the given {@code pdfId}
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html">Spring MultipartFile</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public PdfDocument getOrCreatePdfDocument(MultipartFile pdfData, Long pdfId) {
        if (pdfData == null) {
            if (pdfId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No PDF file provided and no existing PDF file found");
            }
            else return getPdfDocumentById(pdfId);
        }
        return createPdfDocument(pdfData);
    }

    /**
     * This method creates a {@link PdfDocument} with the given {@code pdfData}
     * @param pdfData {@code MultipartFile}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if {@code pdfData} is {@code null} or {@code empty}
     * @return the created {@link PdfDocument}
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html">Spring MultipartFile</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public PdfDocument createPdfDocument(MultipartFile pdfData) {
        if (pdfData == null || pdfData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No PDF file provided or file is empty");
        }
        try {
            return new PdfDocument(pdfData.getOriginalFilename(), pdfData.getBytes());
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading PDF file", ioException);
        }
    }

    /**
     * This method gets the {@code byte[]} of the {@link PdfDocument} with the given {@code id}
     * @param id {@code Long}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if the {@link PdfDocument} could not be found
     * @return the {@code byte[]} of the {@link PdfDocument} with the given {@code id}
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public byte[] getPdfDocumentDataById(Long id) {
        PdfDocument pdfDocument = getPdfDocumentById(id);
        return pdfDocument.getPdfData();
    }

    /**
     * This method gets the {@code name} of the {@link PdfDocument} with the given {@code id}
     * @param id {@code Long}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if the {@link PdfDocument} could not be found
     * @return the {@code name} of the {@link PdfDocument} with the given {@code id}
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String getPdfDocumentNameById(Long id) {
        PdfDocument pdfDocument = getPdfDocumentById(id);
        return pdfDocument.getName();
    }

    /**
     * This method gets the {@link PdfDocument} with the given {@code id}
     * @param id {@code Long}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if the {@link PdfDocument} could not be found
     * @return the {@link PdfDocument} with the given {@code id}
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public PdfDocument getPdfDocumentById(Long id) {
        return pdfDocumentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PDF document not found with id: " + id));
    }
}
