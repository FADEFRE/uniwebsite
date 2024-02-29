package swtp12.modulecrediting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ModulesConnectionDTO;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ModulesConnectionServiceTest {

    @Mock
    private ModulesConnectionRepository modulesConnectionRepository;

    @Mock
    private ExternalModuleService externalModuleService;

    @Mock
    private ModuleLeipzigService moduleLeipzigService;

    @InjectMocks
    private ModulesConnectionService modulesConnectionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateModulesConnectionsWithDuplicate() {
        ModulesConnectionDTO dto = new ModulesConnectionDTO();
        List<ModulesConnectionDTO> dtos = new ArrayList<>();
        dtos.add(dto);

        when(externalModuleService.createExternalModules(any())).thenReturn(new ArrayList<>());
        when(moduleLeipzigService.getModulesLeipzigByNames(any())).thenReturn(new ArrayList<>());

        List<ModulesConnection> result = modulesConnectionService.createModulesConnectionsWithDuplicate(dtos);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateModulesConnection_AllExternalModulesNull() {
    ModulesConnectionDTO dto = new ModulesConnectionDTO();
    dto.setId(1L);
    dto.setExternalModules(null); // Setting external modules to null
    List<ModulesConnectionDTO> dtos = new ArrayList<>();
    dtos.add(dto);

    ModulesConnection connection = new ModulesConnection();
    ModulesConnection originalConnection = new ModulesConnection();
    connection.setModulesConnectionOriginal(originalConnection);
    when(modulesConnectionRepository.findById(any())).thenReturn(java.util.Optional.of(connection));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
        modulesConnectionService.updateModulesConnection(dtos, "study-office");
    });

    assertEquals(HttpStatus.FORBIDDEN.value(), exception.getStatusCode().value());
    assertEquals("You cant delete all External Modules of a Modules Connection 1", exception.getReason());
    verify(modulesConnectionRepository, never()).save(any());
    verify(externalModuleService, never()).updateExternalModules(any());
    verify(moduleLeipzigService, never()).updateRelationModulesConnectionToModulesLeipzig(any(), any());
}

    @Test
    public void testUpdateModulesConnection_NormalCase() {
    ModulesConnectionDTO dto = new ModulesConnectionDTO();
    dto.setId(1L);
    dto.setExternalModules(new ArrayList<>()); // Setting external modules to a non-null value
    List<ModulesConnectionDTO> dtos = new ArrayList<>();
    dtos.add(dto);

    ModulesConnection connection = new ModulesConnection();
    ModulesConnection originalConnection = new ModulesConnection();
    connection.setModulesConnectionOriginal(originalConnection);
    when(modulesConnectionRepository.findById(any())).thenReturn(java.util.Optional.of(connection));

    modulesConnectionService.updateModulesConnection(dtos, "study-office");

}


    @Test
    public void testDeleteOriginalModulesConnections() {
        List<ModulesConnection> connections = new ArrayList<>();

        modulesConnectionService.deleteOriginalModulesConnections(connections);

        verify(modulesConnectionRepository, times(connections.size())).save(any());
    }

    @Test
    public void testGetOriginalModulesConnections() {
        List<ModulesConnection> connections = new ArrayList<>();
        ModulesConnection connection = new ModulesConnection();
        connections.add(connection);

        List<ModulesConnection> result = modulesConnectionService.getOriginalModulesConnections(connections);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetOriginalModulesConnectionsWithFormalRejectionData() {
        List<ModulesConnection> connections = new ArrayList<>();
        ModulesConnection connection = new ModulesConnection();
        connection.setModulesConnectionOriginal(new ModulesConnection());
        connection.setFormalRejection(true);
        connection.setFormalRejectionComment("Rejection Comment");
        connections.add(connection);

        List<ModulesConnection> result = modulesConnectionService.getOriginalModulesConnectionsWithFormalRejectionData(connections);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getFormalRejection());
        assertEquals("Rejection Comment", result.get(0).getFormalRejectionComment());
    }
}
