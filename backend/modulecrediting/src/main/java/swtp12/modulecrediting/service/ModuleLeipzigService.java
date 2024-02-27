package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
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
    public void updateRelationModulesConnectionToModulesLeipzig(ModulesConnection modulesConnection, List<ModuleLeipzigDTO> modulesLeipzigDTO) {
        for(ModuleLeipzigDTO ml : modulesLeipzigDTO) {
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

    public ArrayList<ModuleLeipzig> getModulesLeipzigByNames(List<ModuleLeipzigDTO> moduleNamesLeipzig) {
        if(moduleNamesLeipzig == null || moduleNamesLeipzig.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Leipzig Names provided");

        ArrayList<ModuleLeipzig> modulesLeipzig = new ArrayList<>();
        for(ModuleLeipzigDTO ml : moduleNamesLeipzig) {
            modulesLeipzig.add(getModuleLeipzigByName(ml.getName()));
        }
        return modulesLeipzig;
    }

    public ModuleLeipzig getModuleLeipzigByName(String name) {
        Optional<ModuleLeipzig> moduleLeipzig = moduleLeipzigRepository.findByName(name);
        if(moduleLeipzig.isPresent())
            return moduleLeipzig.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with moduleName: " + name);
    }

    public String createModuleLeipzig(ModuleLeipzigDTO moduleLeipzigDTO) {
        if (moduleLeipzigDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigDTO.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");

        String moduleCode = "";
        if (!moduleLeipzigDTO.getCode().isBlank()) {
            moduleCode = moduleLeipzigDTO.getCode();

            Optional<ModuleLeipzig> moduleLeipzigCodeOptional = moduleLeipzigRepository.findByCode(moduleCode);
            if (moduleLeipzigCodeOptional.isPresent()) {
                if (moduleLeipzigCodeOptional.get().getIsActive())
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this code already exists");
            }
        }

        String moduleName = moduleLeipzigDTO.getName();
        Optional<ModuleLeipzig> moduleLeipzigNameOptional = moduleLeipzigRepository.findByName(moduleName);
        if (moduleLeipzigNameOptional.isPresent()) {
            ModuleLeipzig moduleLeipzig = moduleLeipzigNameOptional.get();
            if (moduleLeipzig.getIsActive())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this name already exists");

            // reactivate
            moduleLeipzig.setIsActive(true);
            moduleLeipzig.setCode(moduleCode);
            
            moduleLeipzigRepository.save(moduleLeipzig);
            return moduleLeipzig.getName();
        }

        // create new module
        ModuleLeipzig moduleLeipzig = new ModuleLeipzig(moduleName, moduleCode);
        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleLeipzig.getName();
    }

    public String updateModuleLeipzig(String name, ModuleLeipzigDTO moduleLeipzigDTO) {
        if (moduleLeipzigDTO == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigDTO.getName() == null || moduleLeipzigDTO.getCode() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module name or code is null");
        if (moduleLeipzigDTO.getName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");

        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);

        if (!moduleLeipzig.getIsActive()) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is inactive");

        Optional<ModuleLeipzig> possibleConflictModuleName = moduleLeipzigRepository.findByName(moduleLeipzigDTO.getName());
        if (possibleConflictModuleName.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this name already exists");

        String moduleCode = "";
        if (!moduleLeipzigDTO.getCode().isBlank()) {
            Optional<ModuleLeipzig> possibleConflictModuleCode = moduleLeipzigRepository.findByName(moduleLeipzigDTO.getCode());
            if (possibleConflictModuleCode.isPresent())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this code already exists");
            
            moduleCode = moduleLeipzigDTO.getCode();
        }

        moduleLeipzig.setName(moduleLeipzigDTO.getName());
        moduleLeipzig.setCode(moduleCode);
        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleLeipzig.getName();
    }

    public String deleteModuleLeipzig(String name) {
        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);
        if (!moduleLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Leipzig is already deactivated with name: " + name);

        moduleLeipzig.setIsActive(false);

        if (!checkIfModuleIsUsedInApplications(moduleLeipzig)) {
            System.out.println("Delete Module Leipzig: " + moduleLeipzig.getName() + ", " + moduleLeipzig.getCode());
            moduleLeipzigRepository.deleteById(moduleLeipzig.getId());
            return "DELETED";
        }

        System.out.println("Deactivate Module Leipzig: " + moduleLeipzig.getName() + ", " + moduleLeipzig.getCode());
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
