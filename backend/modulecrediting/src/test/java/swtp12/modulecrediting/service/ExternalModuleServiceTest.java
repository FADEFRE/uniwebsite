package swtp12.modulecrediting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ExternalModuleDTO;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.ExternalModuleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ExternalModuleServiceTest {

    @Mock
    private PdfDocumentService pdfDocumentService;

    @Mock
    private ExternalModuleRepository externalModuleRepository;

    @InjectMocks
    private ExternalModuleService externalModuleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
void testCreateExternalModules() {
    
    ExternalModuleDTO dto1 = new ExternalModuleDTO();
    dto1.setName("Modulname");
    dto1.setUniversity("Universitätsname");
    dto1.setPoints(5);
    dto1.setPointSystem("ECTS");
    MultipartFile descriptionFile1 = null; 
    dto1.setDescription(descriptionFile1);

    //  DTO without name
    ExternalModuleDTO dto2 = new ExternalModuleDTO();
    dto2.setUniversity("Universitätsname");
    dto2.setPoints(5);
    dto2.setPointSystem("ECTS");
    MultipartFile descriptionFile2 = null; 
    dto2.setDescription(descriptionFile2);

    //  DTO without uni
    ExternalModuleDTO dto3 = new ExternalModuleDTO();
    dto3.setName("Modulname");
    dto3.setPoints(5);
    dto3.setPointSystem("ECTS");
    MultipartFile descriptionFile3 = null; 
    dto3.setDescription(descriptionFile3);

    // DTO without points
    ExternalModuleDTO dto4 = new ExternalModuleDTO();
    dto4.setName("Modulname");
    dto4.setUniversity("Universitätsname");
    dto4.setPointSystem("ECTS");
    MultipartFile descriptionFile4 = null; 
    dto4.setDescription(descriptionFile4);

    // DTO wtihout pointsystem
    ExternalModuleDTO dto5 = new ExternalModuleDTO();
    dto5.setName("Modulname");
    dto5.setUniversity("Universitätsname");
    dto5.setPoints(5);
    MultipartFile descriptionFile5 = null; 
    dto5.setDescription(descriptionFile5);

    List<ExternalModuleDTO> externalModuleDTOs = new ArrayList<>();
    externalModuleDTOs.add(dto1);
    externalModuleDTOs.add(dto2); 
    externalModuleDTOs.add(dto3);
    externalModuleDTOs.add(dto4);
    externalModuleDTOs.add(dto5);

    PdfDocument pdfDocument = new PdfDocument();
    pdfDocument.setId(1L); 
    pdfDocument.setName("Beispiel-PDF-Name"); 
    pdfDocument.setPdfData(new byte[]{0x01, 0x02, 0x03}); 
    when(pdfDocumentService.createOrGetPdfDocument(any(MultipartFile.class), anyLong())).thenReturn(pdfDocument);

    when(externalModuleRepository.save(any(ExternalModule.class))).thenReturn(new ExternalModule());

    assertThrows(ResponseStatusException.class, () -> externalModuleService.createExternalModules(externalModuleDTOs));
}



    @Test
    void testUpdateExternalModules() {
        ExternalModuleDTO dto = new ExternalModuleDTO();
        dto.setId(1L);
        dto.setName("Updated Module Name");
        dto.setUniversity("Updated University Name");
        dto.setPoints(8);
        dto.setPointSystem("Updated ECTS");

        List<ExternalModuleDTO> externalModuleUpdateDTOs = new ArrayList<>();
        externalModuleUpdateDTOs.add(dto);

        ExternalModule existingModule = new ExternalModule();
        existingModule.setId(dto.getId());
        existingModule.setName("Original Module Name");
        existingModule.setUniversity("Original University Name");
        existingModule.setPoints(5);
        existingModule.setPointSystem("Original ECTS");

        when(externalModuleRepository.findById(dto.getId())).thenReturn(Optional.of(existingModule));

        externalModuleService.updateExternalModules(externalModuleUpdateDTOs);

        assertEquals(dto.getName(), existingModule.getName());
        assertEquals(dto.getUniversity(), existingModule.getUniversity());
        assertEquals(dto.getPoints(), existingModule.getPoints());
        assertEquals(dto.getPointSystem(), existingModule.getPointSystem());
    }
}
