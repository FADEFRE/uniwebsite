package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleLeipzigService {
    @Autowired
    private ModuleLeipzigRepository moduleLeipzigRepository;
    public ArrayList<ModuleLeipzig> getModulesLeipzigByNames(List<String> moduleNamesLeipzig) {
        ArrayList<ModuleLeipzig> modulesLeipzig = new ArrayList();
        for(String name : moduleNamesLeipzig) {
            Optional<ModuleLeipzig> moduleLeipzig = moduleLeipzigRepository.findByModuleName(name);
            if(moduleLeipzig.isPresent()) {
                modulesLeipzig.add(moduleLeipzig.get());
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with moduleName: " + name);
            }
        }
        return modulesLeipzig;
    }
}
