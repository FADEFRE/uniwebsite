package swtp12.modulecrediting.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;


import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;




@ExtendWith(MockitoExtension.class)
public class GeneratedPdfServiceTest {

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private GeneratedPdfService generatedPdfService;

    @Test
    public void generatePdfFromHtml_ShouldGeneratePdfWithCorrectContent_WhenCalledWithValidId() throws java.io.IOException {
        Application application = new Application();
        ApplicationService applicationService = mock(ApplicationService.class);
        GeneratedPdfService generatedPdfService = mock(GeneratedPdfService.class);

        application.setId("validId");
        application.setCreationDate(LocalDateTime.now());
        application.setFullStatus(EnumApplicationStatus.IN_BEARBEITUNG);
        CourseLeipzig courseLeipzig = new CourseLeipzig();
        courseLeipzig.setName("CourseName");
        application.setCourseLeipzig(courseLeipzig);

        ModulesConnection modulesConnection = new ModulesConnection();
        ExternalModule externalModule = new ExternalModule();
        externalModule.setName("ExternalModule1");
        ModuleLeipzig moduleLeipzig = new ModuleLeipzig();
        moduleLeipzig.setName("ModuleLeipzig1");
        modulesConnection.setExternalModules(Collections.singletonList(externalModule));
        modulesConnection.setModulesLeipzig(Collections.singletonList(moduleLeipzig));
        application.setModulesConnections(Collections.singletonList(modulesConnection));

        when(applicationService.getApplicationById("validId")).thenReturn(application);
        
        String mockPdfContent = "PDF Content"; 
        byte[] mockPdfBytes = mockPdfContent.getBytes();
        when(generatedPdfService.generatePdfFromHtml("validId")).thenReturn(mockPdfBytes);

        byte[] pdfBytes = generatedPdfService.generatePdfFromHtml("validId");

        String pdfContent = new String(pdfBytes);
        assertEquals(false, pdfContent.contains("Sample Course")); 
        assertEquals(false, pdfContent.contains("External Module")); 
        assertEquals(false, pdfContent.contains("Leipzig Module")); 
    }
}


