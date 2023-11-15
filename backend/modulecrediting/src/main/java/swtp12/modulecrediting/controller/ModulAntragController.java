package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModulAntrag;
import swtp12.modulecrediting.repository.ModulAntragRepository;

@RestController
@RequestMapping("/modulantrag")
public class ModulAntragController {
    
    private final ModulAntragRepository modulAntragRepository;

    public ModulAntragController(ModulAntragRepository modulAntragRepository) {
        this.modulAntragRepository = modulAntragRepository;
    }

    @GetMapping
    List<ModulAntrag> all() {
        return modulAntragRepository.findAll();
    }


}
