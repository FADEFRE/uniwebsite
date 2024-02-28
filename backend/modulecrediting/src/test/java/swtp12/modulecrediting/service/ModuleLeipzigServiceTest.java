package swtp12.modulecrediting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ModuleLeipzigServiceTest {

    @Mock
    private ModuleLeipzigRepository moduleLeipzigRepository;

    @InjectMocks
    private ModuleLeipzigService moduleLeipzigService;

    @Test
void testUpdateModuleLeipzig_Success() {
    ModuleLeipzig existingModule = new ModuleLeipzig("Test Module", "TEST");

    when(moduleLeipzigRepository.findByName("Test Module")).thenReturn(Optional.of(existingModule));    when(moduleLeipzigRepository.save(any(ModuleLeipzig.class))).thenReturn(new ModuleLeipzig("Updated Test Module", "UPDATED"));

    ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
    moduleLeipzigDTO.setName("Updated Test Module");
    moduleLeipzigDTO.setCode("UPDATED");

    String result = moduleLeipzigService.updateModuleLeipzig("Test Module", moduleLeipzigDTO);

    assertEquals("Updated Test Module", result);
}

    @Test
    void testUpdateModuleLeipzig_NoDataGiven() {
        ModuleLeipzigDTO moduleLeipzigDTO = null;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.updateModuleLeipzig("Test Module", moduleLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No data given", exception.getReason());
    }

    @Test
    void testUpdateModuleLeipzig_NullModuleName() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.updateModuleLeipzig("Test Module", moduleLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Module name or code is null", exception.getReason());
    }

    @Test
    void testUpdateModuleLeipzig_BlankModuleName() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
    
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.updateModuleLeipzig("Test Module", moduleLeipzigDTO);
        });
    
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Module name or code is null", exception.getReason());
    }

    @Test
    void testUpdateModuleLeipzig_InactiveModule() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
        ModuleLeipzig existingModule = new ModuleLeipzig("Test Module", "TEST");
        existingModule.setIsActive(false);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.updateModuleLeipzig("Test Module", moduleLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Module name or code is null", exception.getReason());
    }

    @Test
    void testUpdateModuleLeipzig_ConflictingModuleName() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.updateModuleLeipzig("Test Module", moduleLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Module name or code is null", exception.getReason());
    }

    @Test
    void testUpdateModuleLeipzig_ConflictingModuleCode() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.updateModuleLeipzig("Test Module", moduleLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Module name or code is null", exception.getReason());
    }
}

