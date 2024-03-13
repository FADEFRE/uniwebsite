package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.util.LogUtil;



@Service
public class ModuleLeipzigService {
ModuleLeipzigRepository moduleLeipzigRepository;
    private ModulesConnectionService modulesConnectionService;

    public ModuleLeipzigService(ModuleLeipzigRepository moduleLeipzigRepository, @Lazy ModulesConnectionService modulesConnectionService) {
        this.moduleLeipzigRepository = moduleLeipzigRepository;
        this.modulesConnectionService = modulesConnectionService;
    }

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

    public List<ModuleLeipzig> getAllModulesLeipzig() {
        return moduleLeipzigRepository.findAll();
    }

    public List<ModuleLeipzig> getModulesLeipzigByNames(List<ModuleLeipzigDTO> moduleNamesLeipzig) {
        if(moduleNamesLeipzig == null || moduleNamesLeipzig.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Leipzig Names provided");

        List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();
        for(ModuleLeipzigDTO ml : moduleNamesLeipzig) {
            modulesLeipzig.add(getModuleLeipzigByName(ml.getName()));
        }
        return modulesLeipzig;
    }

    public ModuleLeipzig getModuleLeipzigByName(String name) {
        ModuleLeipzig moduleLeipzig = moduleLeipzigRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with moduleName: " + name));
        return moduleLeipzig;
    }

    public String getModuleLeipzigNameById(Long id) {
        ModuleLeipzig moduleLeipzig = moduleLeipzigRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with given id: " + id));
        return moduleLeipzig.getName();
    }

    public ModuleLeipzig findOrCreateNewModuleLeipzig(String name, String code) {
        Optional<ModuleLeipzig> moduleOptional = moduleLeipzigRepository.findByName(name);
        if (!moduleOptional.isPresent()) {
            if (code != null && !code.isBlank()) {
                moduleLeipzigRepository.findByCode(code).ifPresent(m -> {
                    m.setCode(m.getCode() + "toChange");
                    moduleLeipzigRepository.save(m);
                });
            }
            LogUtil.printModuleLog(LogUtil.ModuleType.CREATED, name, code, null, null);
            return moduleLeipzigRepository.save(new ModuleLeipzig(name, code));
        }
        else {
            ModuleLeipzig moduleLeipzig = moduleOptional.get();
            if (!moduleLeipzig.getIsActive()) {
                moduleLeipzig.setIsActive(true);
                LogUtil.printModuleLog(LogUtil.ModuleType.REACTIVATED, name, code, null, null);
            }
            else {
                LogUtil.printModuleLog(LogUtil.ModuleType.FOUND, name, code, null, null);
            }
            
            if (code != null && !code.isBlank()) {
                if (!moduleLeipzig.getCode().equals(code)) {
                    moduleLeipzigRepository.findByCode(code).ifPresent(m -> {
                        String newCode = m.getCode() + "toChange";
                        LogUtil.printModuleLog(LogUtil.ModuleType.UPDATED, m.getName(), m.getCode(), m.getName(), newCode);
                        m.setCode(newCode);
                        moduleLeipzigRepository.save(m);
                    });
                    LogUtil.printModuleLog(LogUtil.ModuleType.UPDATED, name, moduleLeipzig.getCode(), name, code);
                    moduleLeipzig.setCode(code);
                    return moduleLeipzigRepository.save(moduleLeipzig);
                }
            }
            return moduleLeipzig;
        }
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
            LogUtil.printModuleLog(LogUtil.ModuleType.REACTIVATED, moduleLeipzig.getName(), moduleLeipzig.getCode(), null, null);
            moduleLeipzigRepository.save(moduleLeipzig);
            return moduleLeipzig.getName();
        }

        // create new module
        ModuleLeipzig moduleLeipzig = new ModuleLeipzig(moduleName, moduleCode);
        LogUtil.printModuleLog(LogUtil.ModuleType.CREATED, moduleLeipzig.getName(), moduleLeipzig.getCode(), null, null);
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
        if (possibleConflictModuleName.isPresent() && !name.equals(moduleLeipzigDTO.getName()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this name already exists");

        String moduleCode = "";
        if (!moduleLeipzigDTO.getCode().isBlank()) {
            Optional<ModuleLeipzig> possibleConflictModuleCode = moduleLeipzigRepository.findByCode(moduleLeipzigDTO.getCode());
            if (possibleConflictModuleCode.isPresent() && possibleConflictModuleCode.get().getName().equals(name))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this code already exists");
            
            moduleCode = moduleLeipzigDTO.getCode();
        }

        LogUtil.printModuleLog(LogUtil.ModuleType.UPDATED, name, moduleLeipzig.getCode(), moduleLeipzigDTO.getName(), moduleLeipzigDTO.getCode());
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
            LogUtil.printModuleLog(LogUtil.ModuleType.DELETED, moduleLeipzig.getName(), moduleLeipzig.getCode(), null, null);
            moduleLeipzigRepository.deleteById(moduleLeipzig.getId());
            return "DELETED";
        }

        LogUtil.printModuleLog(LogUtil.ModuleType.DEACTIVATED, moduleLeipzig.getName(), moduleLeipzig.getCode(), null, null);
        moduleLeipzigRepository.save(moduleLeipzig);
        return "DEACTIVATED";
    }

    private Boolean checkIfModuleIsUsedInApplications(ModuleLeipzig moduleLeipzig) {
        List<ModulesConnection> modulesConnections = modulesConnectionService.getAllModulesConnections();

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
