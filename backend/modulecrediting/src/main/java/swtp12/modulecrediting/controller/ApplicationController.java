package swtp12.modulecrediting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp12.modulecrediting.dto.ModuleBlockDTO;
import swtp12.modulecrediting.service.ApplicationService;

import java.util.ArrayList;

@RestController
@CrossOrigin
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;


    @PostMapping(value = "/application")
    public ResponseEntity<Integer> createApplication(@ModelAttribute ArrayList<ModuleBlockDTO> moduleBlockDTOList) {
        return ResponseEntity.ok(applicationService.createApplication(moduleBlockDTOList));
    }
}
