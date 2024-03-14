package swtp12.modulecrediting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import swtp12.modulecrediting.dto.ApplicationDTO;
import swtp12.modulecrediting.dto.EnumStatusChangeAllowed;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static swtp12.modulecrediting.model.EnumApplicationStatus.NEU;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private CourseLeipzigService courseLeipzigService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ModulesConnectionService modulesConnectionService;

    @Mock
    private Application application;

    @Spy
    @InjectMocks
    private ApplicationService applicationService;

    private ApplicationDTO applicationDTO;
    private final String applicationId = "applicationId";
    private final EnumApplicationStatus STUDIENBÜRO = EnumApplicationStatus.STUDIENBÜRO; 
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        application = new Application();
        application.setId(applicationId);
        application.setFullStatus(EnumApplicationStatus.FORMFEHLER);

        applicationDTO = new ApplicationDTO();
        applicationDTO.setModulesConnections(Collections.emptyList());

    }

    @Test
    void createApplication_ShouldCreateAndSaveApplication() {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setCourseLeipzig("Test Course");
        applicationDTO.setModulesConnections(List.of(/* ModuleConnectionDTOs */));

        String expectedId = "validId";
        Application expectedApplication = new Application(expectedId);
        CourseLeipzig expectedCourseLeipzig = new CourseLeipzig();
        List<ModulesConnection> expectedModulesConnections = List.of(/* ModulesConnection instances */);

        when(applicationService.generateValidApplicationId()).thenReturn(expectedId);
        when(courseLeipzigService.getCourseLeipzigByName(anyString())).thenReturn(expectedCourseLeipzig);
        when(modulesConnectionService.createModulesConnectionsWithDuplicate(anyList())).thenReturn(expectedModulesConnections);
        when(applicationRepository.save(any(Application.class))).thenReturn(expectedApplication);

        try (MockedStatic<LogUtil> mockedLogUtil = Mockito.mockStatic(LogUtil.class)) {
            String resultId = applicationService.createApplication(applicationDTO);

            assertEquals(expectedId, resultId);
            verify(applicationRepository).save(any(Application.class));
            verify(courseLeipzigService).getCourseLeipzigByName(applicationDTO.getCourseLeipzig());
            verify(modulesConnectionService).createModulesConnectionsWithDuplicate(applicationDTO.getModulesConnections());

            mockedLogUtil.verify(() -> LogUtil.printApplicationLog(LogUtil.ApplicationType.CREATED, expectedId));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void updateApplicationAfterFormalRejection_ShouldUpdateApplicationWhenStatusIsFormfehler() {
        String id = "someId";
        ApplicationDTO applicationDTO = mock(ApplicationDTO.class);
        Application application = mock(Application.class);
        List<ModulesConnection> modulesConnections = mock(List.class);

        when(application.getFullStatus()).thenReturn(EnumApplicationStatus.FORMFEHLER);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(modulesConnectionService.createModulesConnectionsWithDuplicate(anyList())).thenReturn(modulesConnections);
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        assertDoesNotThrow(() -> applicationService.updateApplicationAfterFormalRejection(id, applicationDTO));
        verify(applicationRepository).save(application);
    }

    @Test
    void updateApplication_ShouldSetStatusToStudienbuero_WhenStatusIsNeuAndUserRoleIsStudyOffice() {
        String id = "validId";
        String userRole = "study-office";
        ApplicationDTO applicationDTO = mock(ApplicationDTO.class);
        Application application = mock(Application.class);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(application.getFullStatus()).thenReturn(NEU);

        applicationService.updateApplication(id, applicationDTO, userRole);

        verify(application).setFullStatus(STUDIENBÜRO);
        verify(applicationRepository).save(application);
    }

    @Test
    void updateApplication_ShouldSetStatusToPruefungsausschuss_WhenStatusIsNeuAndUserRoleIsChairman() {
        String id = "validId";
        String userRole = "chairman";
        ApplicationDTO applicationDTO = mock(ApplicationDTO.class);
        Application application = mock(Application.class);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(application.getFullStatus()).thenReturn(NEU);

        applicationService.updateApplication(id, applicationDTO, userRole);

        verify(application).setFullStatus(EnumApplicationStatus.PRÜFUNGSAUSSCHUSS);
        verify(applicationRepository).save(application);
    }

    @Test
    void updateApplicationStatusAllowed_ShouldReturnReject_WhenStatusIsStudienbueroOrNeuAndContainsFormalRejection() {
        String id = "validId";
        Application application = new Application();
        application.setFullStatus(EnumApplicationStatus.STUDIENBÜRO); // Use the real enum value

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        doReturn(true).when(applicationService).containsFormalRejection(application);

        EnumStatusChangeAllowed result = applicationService.updateApplicationStatusAllowed(id);

        assertEquals(EnumStatusChangeAllowed.REJECT, result);
    }

    @Test
    void updateApplicationStatusAllowed_ShouldReturnNotAllowed_WhenStatusIsAbgeschlossen() {
        String id = "validId";
        Application application = new Application();
        application.setFullStatus(EnumApplicationStatus.ABGESCHLOSSEN);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

        EnumStatusChangeAllowed result = applicationService.updateApplicationStatusAllowed(id);

        assertEquals(EnumStatusChangeAllowed.NOT_ALLOWED, result);
    }


    @Test
    void updateApplicationStatusAllowed_ShouldReturnPassOn_WhenAllDecisionsFinalEditedAndStatusIsPruefungsausschussOrStudienbueroOrNeu() {
        String id = "validId";
        Application application = new Application();
        application.setFullStatus(EnumApplicationStatus.PRÜFUNGSAUSSCHUSS);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        doReturn(true).when(applicationService).allDecisionsFinalEdited(application);
        doReturn(false).when(applicationService).containsFormalRejection(application);

        EnumStatusChangeAllowed result = applicationService.updateApplicationStatusAllowed(id);

        assertEquals(EnumStatusChangeAllowed.PASS_ON, result);
    }

    @Test
    void updateApplicationStatusAllowed_ShouldReturnPassOn_WhenAllDecisionSuggestionEditedAndStatusIsStudienbueroOrNeu() {
        String id = "validId";
        Application application = new Application();
        application.setFullStatus(EnumApplicationStatus.STUDIENBÜRO);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        doReturn(true).when(applicationService).allDecisionSuggestionEdited(application);
        doReturn(false).when(applicationService).containsFormalRejection(application);

        EnumStatusChangeAllowed result = applicationService.updateApplicationStatusAllowed(id);

        assertEquals(EnumStatusChangeAllowed.PASS_ON, result);
    }


    @Test
    void updateApplicationStatus_ShouldSetStatusToFormfehler_WhenContainsFormalRejectionIsTrue() {
        String id = "validId";
        Application application = new Application();
        application.setId(id);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        doReturn(true).when(applicationService).containsFormalRejection(application);
        when(applicationRepository.save(any(Application.class))).thenReturn(application); 

        try (MockedStatic<LogUtil> mockedLogUtil = Mockito.mockStatic(LogUtil.class)) {
            EnumApplicationStatus result = applicationService.updateApplicationStatus(id);

            assertEquals(EnumApplicationStatus.FORMFEHLER, result);
            verify(applicationRepository).save(application);
            verify(applicationService).containsFormalRejection(application);

            mockedLogUtil.verify(() -> LogUtil.printApplicationLog(LogUtil.ApplicationType.FORMAL_REJECTION, application.getId()));
        }

    }

    @Test
    void getApplicationStudentById_ShouldReturnEditedApplication_WhenStatusIsAbgeschlossen() {
        String id = "validId";
        Application application = new Application();
        application.setId(id);
        application.setFullStatus(EnumApplicationStatus.ABGESCHLOSSEN);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

        Application result = applicationService.getApplicationStudentById(id);

        assertEquals(EnumApplicationStatus.ABGESCHLOSSEN, result.getFullStatus());
    }

    @Test
    void getApplicationStudentById_ShouldReturnOriginalApplicationWithRejectionInfo_WhenStatusIsFormfehler() {
        String id = "validId";
        Application application = new Application();
        application.setId(id);
        application.setFullStatus(EnumApplicationStatus.FORMFEHLER);
        List<ModulesConnection> originalModulesConnectionsWithRejection = new ArrayList<>();

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(modulesConnectionService.getOriginalModulesConnectionsWithFormalRejectionData(any())).thenReturn(originalModulesConnectionsWithRejection);

        Application result = applicationService.getApplicationStudentById(id);

        assertEquals(EnumApplicationStatus.FORMFEHLER, result.getFullStatus());
        assertEquals(originalModulesConnectionsWithRejection, result.getModulesConnections());
    }

}
