
package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

@RestController
@RequestMapping("/modules-leipzig")
public class ModuleLeipzigController {

    @Autowired
    private ModuleLeipzigRepository modulLeipzigRepository;

    @GetMapping
    List<ModuleLeipzig> getModulesLeipzig() {
        return modulLeipzigRepository.findAll();
    }

}
