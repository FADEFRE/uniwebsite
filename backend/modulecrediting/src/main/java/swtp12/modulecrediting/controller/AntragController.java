package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.Antrag;
import swtp12.modulecrediting.repository.AntragRepository;

@RestController
@RequestMapping("/antrag")
public class AntragController {
    
    private final AntragRepository antragRepository;

    public AntragController(AntragRepository antragRepository) {
        this.antragRepository = antragRepository;
    }

    @GetMapping
    List<Antrag> all() {
        return antragRepository.findAll();
    }


}
