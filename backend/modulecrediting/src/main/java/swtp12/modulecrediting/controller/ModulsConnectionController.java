package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModulsConnection;
import swtp12.modulecrediting.repository.ModulsConnectionRepository;

@RestController
@RequestMapping("/modulsconnection")
public class ModulsConnectionController {
    
    private final ModulsConnectionRepository modulsConnectionRepository;

    public ModulsConnectionController(ModulsConnectionRepository modulsConnectionRepository) {
        this.modulsConnectionRepository = modulsConnectionRepository;
    }

    @GetMapping
    List<ModulsConnection> all() {
        return modulsConnectionRepository.findAll();
    }


}
