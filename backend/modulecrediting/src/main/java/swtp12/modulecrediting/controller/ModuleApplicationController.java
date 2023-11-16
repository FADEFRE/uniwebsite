package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.repository.ModuleApplicationRepository;

@RestController
@CrossOrigin
@RequestMapping("/moduleapplication")
public class ModuleApplicationController {

    @Autowired
    private ModuleApplicationRepository modulAntragRepository;

    @GetMapping
    List<ModuleApplication> all() {
        return modulAntragRepository.findAll();
    }


}
