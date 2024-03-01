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


    // CreateModules - Test
    @Test
    void testCreateModuleLeipzig_NoDataGiven() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();

        assertThrows(java.lang.NullPointerException.class, () -> {
            moduleLeipzigService.createModuleLeipzig(moduleLeipzigDTO);
        }, "No data given");
    }

    @Test
    void testCreateModuleLeipzig_NoModuleNameGiven() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
        moduleLeipzigDTO.setName("");
        moduleLeipzigDTO.setCode("TEST");

        assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.createModuleLeipzig(moduleLeipzigDTO);
        }, "No module name given");
    }

    @Test
    void testCreateModuleLeipzig_ModuleWithSameNameExists() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
        moduleLeipzigDTO.setName("Test Module");
        moduleLeipzigDTO.setCode("");

        ModuleLeipzig existingModule = new ModuleLeipzig("Test Module", "TEST");

        when(moduleLeipzigRepository.findByName("Test Module")).thenReturn(Optional.of(existingModule));

        assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.createModuleLeipzig(moduleLeipzigDTO);
        }, "Module with this name already exists");
    }

    @Test
    void testCreateModuleLeipzig_NewModuleCreated() {
        ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
        moduleLeipzigDTO.setName("Test Module 2");
        moduleLeipzigDTO.setCode("TEST2");

        when(moduleLeipzigRepository.findByName("Test Module 2")).thenReturn(Optional.empty());

        String result = moduleLeipzigService.createModuleLeipzig(moduleLeipzigDTO);
        assertEquals("Test Module 2", result);
    }


    // UpdateModules - Test
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

    //DeleteModules - Test
    @Test
    void testDeleteModuleLeipzig_ModuleAlreadyDeactivated() {
        ModuleLeipzig deactivatedModule = new ModuleLeipzig("Test Module", "TEST");
        deactivatedModule.setIsActive(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            moduleLeipzigService.deleteModuleLeipzig("Test Module");
        });
    
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Module Leipzig not found with moduleName: Test Module", exception.getReason());
    }
}

