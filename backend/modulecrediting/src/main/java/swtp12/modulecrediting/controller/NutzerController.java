package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.Nutzer;
import swtp12.modulecrediting.repository.NutzerRepository;

@RestController
@RequestMapping("/nutzer")
public class NutzerController {

    @Autowired
    private NutzerRepository nutzerRepository;

    @GetMapping
    List<Nutzer> all(){
        return nutzerRepository.findAll();
    }
}
