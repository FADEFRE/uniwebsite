package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ModuleLeipzigCreateDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigUpdateDTO;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;



@Service
public class ModuleLeipzigService {
    @Autowired
    private ModuleLeipzigRepository moduleLeipzigRepository;

    @Autowired
    private ModulesConnectionRepository modulesConnectionRepository;

    // used for application update
    public void updateApplicationModulesLeipzig(ModulesConnection modulesConnection, List<ModuleLeipzigUpdateDTO> modulesLeipzigDTO) {
        for(ModuleLeipzigUpdateDTO ml : modulesLeipzigDTO) {
            ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(ml.getName());

            // check if duplicate module leipzig was sent
            if(modulesConnection.getModulesLeipzig().contains(moduleLeipzig))
                continue;

            // add new module leipzig to modules connection
            modulesConnection.addModulesLeipzig(List.of(moduleLeipzig));
        }
    }

    public List<ModuleLeipzig> getModulesLeipzig() {
        return moduleLeipzigRepository.findAll();
    }
    public ArrayList<ModuleLeipzig> getModulesLeipzigByNames(List<String> moduleNamesLeipzig) {
        if(moduleNamesLeipzig.size() == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Leipzig Names provided");

        ArrayList<ModuleLeipzig> modulesLeipzig = new ArrayList<>();
        for(String name : moduleNamesLeipzig) {
            modulesLeipzig.add(getModuleLeipzigByName(name));
        }
        return modulesLeipzig;
    }
    public ModuleLeipzig getModuleLeipzigByName(String name) {
        Optional<ModuleLeipzig> moduleLeipzig = moduleLeipzigRepository.findByName(name);
        if(moduleLeipzig.isPresent())
            return moduleLeipzig.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with moduleName: " + name);
    }

    public String createModuleLeipzig(ModuleLeipzigCreateDTO moduleLeipzigCreateDTO) {
        if (moduleLeipzigCreateDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigCreateDTO.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");
        if (moduleLeipzigCreateDTO.getCode().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No moduel code given");

        String moduleName = moduleLeipzigCreateDTO.getName();
        String moduleCode = moduleLeipzigCreateDTO.getCode();

        Optional<ModuleLeipzig> moduleLeipzigOptional = moduleLeipzigRepository.findByName(moduleName);
        if (moduleLeipzigOptional.isPresent()) {
            ModuleLeipzig moduleLeipzig = moduleLeipzigOptional.get();
            if (moduleLeipzig.getIsActive())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module with this name already exists: " + moduleName);

            // reacitvate old module
            moduleLeipzig.setCode(moduleCode);
            moduleLeipzigRepository.save(moduleLeipzig);
            return moduleLeipzig.getName();
        }

        // create new module
        ModuleLeipzig moduleLeipzig = new ModuleLeipzig(moduleName, moduleCode, true);
        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleLeipzig.getName();
    }

    public String updateModuleLeipzig(String name, ModuleLeipzigCreateDTO moduleLeipzigCreateDTO) {
        if (moduleLeipzigCreateDTO == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigCreateDTO.getName() == null || moduleLeipzigCreateDTO.getName().isBlank() || moduleLeipzigCreateDTO.getCode() == null || moduleLeipzigCreateDTO.getCode().isBlank())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        Optional<ModuleLeipzig> moduleLeipzigOptional = moduleLeipzigRepository.findByName(name);

        if (!moduleLeipzigOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module: " + name + " exists");

        ModuleLeipzig moduleLeipzig = moduleLeipzigOptional.get();
        moduleLeipzig.setName(moduleLeipzigCreateDTO.getName());
        moduleLeipzig.setCode(moduleLeipzigCreateDTO.getCode());

        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleLeipzig.getName();
    }

    public String deleteModuleLeipzig(String name) {
        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);
        if (!moduleLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with name: " + name);

        moduleLeipzig.setIsActive(false);

        if (!checkIfModuleIsUsedInApplications(moduleLeipzig)) {
            moduleLeipzigRepository.deleteById(moduleLeipzig.getId());
            return "DELETED";
        }

        moduleLeipzigRepository.save(moduleLeipzig);
        return "DEACTIVATED";
    }

    private Boolean checkIfModuleIsUsedInApplications(ModuleLeipzig moduleLeipzig) {
        List<ModulesConnection> modulesConnections = modulesConnectionRepository.findAll();

        if (modulesConnections.isEmpty()) return false;

        for (ModulesConnection mc : modulesConnections) {
            List<ModuleLeipzig> listOfModuleLeipzig = mc.getModulesLeipzig();
            for (ModuleLeipzig ml : listOfModuleLeipzig) {
                if (ml.equals(moduleLeipzig)) return true;
            }
        }

        return false;
    }
}
