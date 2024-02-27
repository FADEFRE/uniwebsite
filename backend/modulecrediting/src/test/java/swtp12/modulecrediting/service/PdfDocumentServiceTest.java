package swtp12.modulecrediting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.repository.PdfDocumentRepository;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PdfDocumentServiceTest {

    @Mock
    private PdfDocumentRepository pdfDocumentRepository;

    @InjectMocks
    private PdfDocumentService pdfDocumentService;

    

    @Test
    public void testCreateOrGetPdfDocument_WithPdfData() throws IOException {
        MultipartFile pdfData = new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", "Test data".getBytes());

        swtp12.modulecrediting.model.PdfDocument result = pdfDocumentService.createOrGetPdfDocument(pdfData, null);

        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
    }

    @Test
    public void testCreateOrGetPdfDocument_WithExistingPdfId() {
        Long pdfId = 123L;

        swtp12.modulecrediting.model.PdfDocument mockPdfDocument = mock(swtp12.modulecrediting.model.PdfDocument.class);

        when(pdfDocumentRepository.findById(pdfId)).thenReturn(Optional.of(mockPdfDocument));

        swtp12.modulecrediting.model.PdfDocument result = pdfDocumentService.createOrGetPdfDocument(null, pdfId);

        assertNotNull(result);
        verify(pdfDocumentRepository, never()).save(any(swtp12.modulecrediting.model.PdfDocument.class));

    }

    @Test
    public void testCreateOrGetPdfDocument_NoPdfDataAndNoPdfId() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pdfDocumentService.createOrGetPdfDocument(null, null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No external module id given", exception.getReason());
        verify(pdfDocumentRepository, never()).findById(any(Long.class));
        verify(pdfDocumentRepository, never()).save(any(swtp12.modulecrediting.model.PdfDocument.class));
    }

    @Test
    public void testCreatePdfDocument_WithPdfData() throws IOException {
        MultipartFile pdfData = new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", "Test data".getBytes());

        swtp12.modulecrediting.model.PdfDocument result = pdfDocumentService.createPdfDocument(pdfData);

        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
    }

    @Test
    public void testCreatePdfDocument_NoPdfData() {
        MultipartFile pdfData = null;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pdfDocumentService.createPdfDocument(pdfData);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No PDF file provided or file is empty", exception.getReason());
    }

    @Test
    public void testCreatePdfDocument_EmptyPdfData() throws IOException {
        MultipartFile pdfData = new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", new byte[0]);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pdfDocumentService.createPdfDocument(pdfData);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No PDF file provided or file is empty", exception.getReason());
    }

    @Test
    public void testCreatePdfDocument_IOException() throws IOException {
        MultipartFile pdfData = mock(MultipartFile.class);
        doThrow(IOException.class).when(pdfData).getBytes();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pdfDocumentService.createPdfDocument(pdfData);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error reading PDF file", exception.getReason());
    }
}

