package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.repository.ModuleApplicationRepository;

@RestController
@RequestMapping("/moduleapplication")
public class ModuleApplicationController {
    
    private final ModuleApplicationRepository modulAntragRepository;

    public ModuleApplicationController(ModuleApplicationRepository modulAntragRepository) {
        this.modulAntragRepository = modulAntragRepository;
    }

    @GetMapping
    List<ModuleApplication> all() {
        return modulAntragRepository.findAll();
    }


}
