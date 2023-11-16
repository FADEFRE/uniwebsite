package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.repository.ApplicationRepository;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    
    private final ApplicationRepository antragRepository;

    public ApplicationController(ApplicationRepository antragRepository) {
        this.antragRepository = antragRepository;
    }

    @GetMapping
    List<Application> all() {
        return antragRepository.findAll();
    }


}
