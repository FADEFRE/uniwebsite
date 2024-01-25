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


    public void updateModulesLeipzig(ModulesConnection modulesConnection, List<ModuleLeipzigUpdateDTO> modulesLeipzigDTO) {
        for(ModuleLeipzigUpdateDTO ml : modulesLeipzigDTO) {
            ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(ml.getName());

            // check if duplicate module leipzig was sent
            if(modulesConnection.getModulesLeipzig().contains(moduleLeipzig))
                continue;

            // add new module leipzig to modules connection
            modulesConnection.addModulesLeipzig(List.of(moduleLeipzig));
        }
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
        Optional<ModuleLeipzig> moduleLeipzig = moduleLeipzigRepository.findById(name);
        if(moduleLeipzig.isPresent())
            return moduleLeipzig.get();

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Leipzig not found with moduleName: " + name);
    }

    public String createModuleLeipzig(ModuleLeipzigCreateDTO moduleLeipzigCreateDTO) {
        if (moduleLeipzigCreateDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigCreateDTO.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");
        if (moduleLeipzigCreateDTO.getCode().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No moduel code given");
        String moduleName = moduleLeipzigCreateDTO.getName();
        String moduelCode = moduleLeipzigCreateDTO.getCode();
        Optional<ModuleLeipzig> mL = moduleLeipzigRepository.findById(moduleName);
        if (mL.isPresent() && mL.get().getCode().equals(moduelCode)) {
            if (mL.get().getIsActive()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course with this name already exists: " + moduleName);
            mL.get().setIsActive(true);
            moduleLeipzigRepository.save(mL.get());
            return "REACTIVATED";
        }

        ModuleLeipzig moduleLeipzig = new ModuleLeipzig(moduleName, moduelCode, true);
        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleName;
    }

    public Boolean deleteModulesLeipzig(String name) {
        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);
        if (!moduleLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Leipzig is already deactivated with name: " + name);
        moduleLeipzig.setIsActive(false);
        if (checkIfDeletionIsPossible(moduleLeipzig)) {
            moduleLeipzigRepository.deleteById(name);
            return true;
        }
        else return false;
    }

    private Boolean checkIfDeletionIsPossible(ModuleLeipzig moduleLeipzig) {
        List<ModulesConnection> allModulesConnections = modulesConnectionRepository.findAll();
        if (allModulesConnections.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are no connections in the database");
        Boolean check = false;
        for (ModulesConnection modulesConnection : allModulesConnections) {
            List<ModuleLeipzig> listOfModuleLeipzig = modulesConnection.getModulesLeipzig();
            for (ModuleLeipzig connectionsModuleLeipzig : listOfModuleLeipzig) {
                if (!connectionsModuleLeipzig.equals(moduleLeipzig)) { check = true; }
                else check = false;
            }
        }
        return check;
    }

    public Boolean getModuleLeipzigState(String name) {
        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);
        return moduleLeipzig.getIsActive();
    }

    public List<ModuleLeipzig> getAllModulesLeipzig() {
        return moduleLeipzigRepository.findAll();
    }

    public Boolean editModule(String name, ModuleLeipzigUpdateDTO moduleLeipzigUpdateDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editModule'");
    }
}
