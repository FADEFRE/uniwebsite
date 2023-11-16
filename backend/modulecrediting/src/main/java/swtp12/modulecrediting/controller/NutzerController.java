package swtp12.modulecrediting.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.Nutzer;
import swtp12.modulecrediting.repository.NutzerRepository;

@RestController
@RequestMapping("/nutzer")
public class NutzerController {
    
    private final NutzerRepository nutzerRepository;

    public NutzerController(NutzerRepository nutzerRepository) {
        this.nutzerRepository = nutzerRepository;
    }

    @GetMapping
    List<Nutzer> all(){
        return nutzerRepository.findAll();
    }
}
