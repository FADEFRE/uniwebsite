package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

@RestController
@RequestMapping("/modulesconnection")
public class ModulesConnectionController {
    
    private final ModulesConnectionRepository modulsConnectionRepository;

    public ModulesConnectionController(ModulesConnectionRepository modulsConnectionRepository) {
        this.modulsConnectionRepository = modulsConnectionRepository;
    }

    @GetMapping
    List<ModulesConnection> all() {
        return modulsConnectionRepository.findAll();
    }


}
