
package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

@RestController
@RequestMapping("/moduleleipzig")
public class ModuleLeipzigController {
    
    private final ModuleLeipzigRepository modulLeipzigRepository;

    public ModuleLeipzigController(ModuleLeipzigRepository modulLeipzigRepository) {
        this.modulLeipzigRepository = modulLeipzigRepository;
    }

    @GetMapping
    List<ModuleLeipzig> all() {
        return modulLeipzigRepository.findAll();
    }

}
