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
        if (moduleLeipzigDTO.getCode().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No moduel code given");

        String moduleName = moduleLeipzigDTO.getName();
        String moduleCode = moduleLeipzigDTO.getCode();

        Optional<ModuleLeipzig> moduleLeipzigOptional = moduleLeipzigRepository.findByName(moduleName);

        if (moduleLeipzigOptional.isPresent()) {
            ModuleLeipzig moduleLeipzig = moduleLeipzigOptional.get();
            if (moduleLeipzig.getIsActive()) throw new ResponseStatusException(HttpStatus.CONFLICT, "The Module already exists:" + moduleLeipzigDTO.getName() );
            else {
                // reactivate with new module code
                moduleLeipzig.setIsActive(true);
                moduleLeipzig.setCode(moduleCode);

                System.out.println("Reactivated Module Leipzig: " + moduleLeipzig.getName() + ", " + moduleLeipzig.getCode());
            }

            moduleLeipzigRepository.save(moduleLeipzig);
            return moduleLeipzig.getName();
        }

        // create new module
        ModuleLeipzig moduleLeipzig = new ModuleLeipzig(moduleName, moduleCode);
        System.out.println("Created new Module Leipzig: " + moduleLeipzig.getName() + ", " + moduleLeipzig.getCode());
        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleLeipzig.getName();
    }

    public String updateModuleLeipzig(String name, ModuleLeipzigDTO moduleLeipzigDTO) {
        if (moduleLeipzigDTO == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigDTO.getName() == null || moduleLeipzigDTO.getName().isBlank() || moduleLeipzigDTO.getCode() == null || moduleLeipzigDTO.getCode().isBlank())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name or module code given || is blank");

        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);

        System.out.print("Update Module Leipzig: " + moduleLeipzig.getName() + ", " + moduleLeipzig.getCode());
        moduleLeipzig.setName(moduleLeipzigDTO.getName());
        moduleLeipzig.setCode(moduleLeipzigDTO.getCode());
        System.out.println(" => " + moduleLeipzig.getName() + ", " + moduleLeipzig.getCode());

        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleLeipzig.getName();
    }

    public String deleteModuleLeipzig(String name) {
        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);
        if (!moduleLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with name: " + name);

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
