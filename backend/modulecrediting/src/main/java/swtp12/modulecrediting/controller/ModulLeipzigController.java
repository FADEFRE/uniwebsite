package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModulLeipzig;
import swtp12.modulecrediting.repository.ModulLeipzigRepository;

@RestController
@RequestMapping("/modulleipzig")
public class ModulLeipzigController {
    
    private final ModulLeipzigRepository modulLeipzigRepository;

    public ModulLeipzigController(ModulLeipzigRepository modulLeipzigRepository) {
        this.modulLeipzigRepository = modulLeipzigRepository;
    }

    @GetMapping
    List<ModulLeipzig> all() {
        return modulLeipzigRepository.findAll();
    }

}
