package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.util.LogUtil;


/**
 * This is a {@code Service} for {@link ModuleLeipzig}
 * @author Frederik Kluge
 * @author Luca Kippe
 * @see #getAllModulesLeipzig
 * @see #getModuleLeipzigByName
 * @see #getModuleLeipzigNameById
 * @see #getOriginalModulesConnectionsWithFormalRejectionData
 * @see #getRelatedModulesConnections
 * @see #createModulesConnectionsWithDuplicate
 * @see #updateModulesConnection
 * @see #deleteOriginalModulesConnections
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Spring Service</a>
 */
@Service
public class ModuleLeipzigService {
    private ModuleLeipzigRepository moduleLeipzigRepository;
    private ModulesConnectionService modulesConnectionService;

    public ModuleLeipzigService(ModuleLeipzigRepository moduleLeipzigRepository, @Lazy ModulesConnectionService modulesConnectionService) {
        this.moduleLeipzigRepository = moduleLeipzigRepository;
        this.modulesConnectionService = modulesConnectionService;
    }


    /**
     * This method returns all {@link ModuleLeipzig} saved in the database
     * @return {@code List} of {@link ModuleLeipzig}
     */
    public List<ModuleLeipzig> getAllModulesLeipzig() {
        return moduleLeipzigRepository.findAll();
    }

    /**
     * This method returns the {@link ModuleLeipzig} with the given {@code name}
     * @param name {@code String}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if the {@link ModulesConnection} could not be found
     * @return {@link ModuleLeipzig} with {@code name} as its name
     * @see ModuleLeipzig
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public ModuleLeipzig getModuleLeipzigByName(String name) {
        ModuleLeipzig moduleLeipzig = moduleLeipzigRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with moduleName: " + name));
        return moduleLeipzig;
    }

    /**
     * This method returns the {@code name} of the {@link ModuleLeipzig} with the given {@code id}
     * @param id {@code Long} 
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if the {@link ModulesConnection} could not be found
     * @return {@code String} name of the {@link ModuleLeipzig} with {@code id} as its id
     * @see ModuleLeipzig
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String getModuleLeipzigNameById(Long id) {
        ModuleLeipzig moduleLeipzig = moduleLeipzigRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Leipzig not found with given id: " + id));
        return moduleLeipzig.getName();
    }

    /**
     * This method returns a {@code List} of {@link ModuleLeipzig} from the given {@code List} of {@link ModuleLeipzigDTO} by their {@code names}
     * @param moduleNamesLeipzig {@code List} of {@link ModuleLeipzigDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code List} of {@link ModuleLeipzigDTO} is {@code null} or {@code empty}
     * @return {@code List} of {@link ModuleLeipzig}
     * @see ModuleLeipzig
     * @see ModuleLeipzigDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public List<ModuleLeipzig> getModulesLeipzigByNamesFromDTO(List<ModuleLeipzigDTO> moduleNamesLeipzig) {
        if(moduleNamesLeipzig == null || moduleNamesLeipzig.size() == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Leipzig Names provided");

        List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();
        moduleNamesLeipzig.forEach(mL -> modulesLeipzig.add(getModuleLeipzigByName(mL.getName())));
        return modulesLeipzig;
    }

    /**
     * This method updates the {@code List} of {@link ModuleLeipzig} in the given {@link ModulesConnection}
     * @param modulesConnection {@link ModulesConnection} to be updated
     * @param modulesLeipzigDTO {@code List} of {@link ModuleLeipzigDTO} 
     * @see ModulesConnection
     * @see ModuleLeipzig
     * @see ModuleLeipzigDTO
     */
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

    /**
     * This method gets the {@link ModuleLeipzig} with the given {@code name} and {@cdoe code} or creates it, in the case it does not already exist
     * <p> If the {@link ModuleLeipzig} already exists but is {@code inactive}, it will be set to {@code isActive = true}
     * @param name {@code String}
     * @param code {@code String}
     * @return {@link ModuleLeipzig} with the given {@code name} and {@cdoe code}
     * @see ModuleLeipzig
     */
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

    /**
     * This method creates a new {@link ModuleLeipzig} definied in the given {@link ModuleLeipzigDTO}
     * @param moduleLeipzigDTO {@link ModuleLeipzigDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link ModuleLeipzigDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code name} field of {@link ModuleLeipzigDTO} is {@code null} or {@code blank}
     * @throws ResponseStatusException with {@code HttpStatus.CONFLICT: 409} if the to be created {@link ModuleLeipzig} already exists AND is already {@code isActive}
     * @return {@code name} of the created {@link ModuleLeipzig}
     * @see ModuleLeipzig
     * @see ModuleLeipzigDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String createModuleLeipzig(ModuleLeipzigDTO moduleLeipzigDTO) {
        if (moduleLeipzigDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigDTO.getName() == null || moduleLeipzigDTO.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");

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

    /**
     * This method updates the data of the {@link ModuleLeipzig} with the given {@code name} with data definied in the given {@link ModuleLeipzigDTO}
     * @param name {@code String}
     * @param moduleLeipzigDTO {@link ModuleLeipzigDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link ModuleLeipzigDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code name} field of {@link ModuleLeipzigDTO} is {@code null} or {@code blank}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code code} field of {@link ModuleLeipzigDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the to be updated {@link ModuleLeipzig} is inactive
     * @throws ResponseStatusException with {@code HttpStatus.CONFLICT: 409} if a {@link ModuleLeipzig} with the possible new {@code name} already exists
     * @throws ResponseStatusException with {@code HttpStatus.CONFLICT: 409} if a {@link ModuleLeipzig} with the possible new {@code code} already exists
     * @return the (possible new) {@code name} of the updated {@link ModuleLeipzig}
     * @see ModuleLeipzig
     * @see ModuleLeipzigDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String updateModuleLeipzig(String name, ModuleLeipzigDTO moduleLeipzigDTO) {
        if (moduleLeipzigDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (moduleLeipzigDTO.getName() == null || moduleLeipzigDTO.getCode() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module name or code is null");
        if (moduleLeipzigDTO.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No module name given");

        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);

        if (!moduleLeipzig.getIsActive()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is inactive");

        Optional<ModuleLeipzig> possibleConflictModuleName = moduleLeipzigRepository.findByName(moduleLeipzigDTO.getName());
        if (possibleConflictModuleName.isPresent() && !name.equals(moduleLeipzigDTO.getName())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this name already exists");

        String moduleCode = "";
        if (!moduleLeipzigDTO.getCode().isBlank()) {
            Optional<ModuleLeipzig> possibleConflictModuleCode = moduleLeipzigRepository.findByCode(moduleLeipzigDTO.getCode());
            if (possibleConflictModuleCode.isPresent() && possibleConflictModuleCode.get().getName().equals(name)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Module with this code already exists");
            
            moduleCode = moduleLeipzigDTO.getCode();
        }

        LogUtil.printModuleLog(LogUtil.ModuleType.UPDATED, name, moduleLeipzig.getCode(), moduleLeipzigDTO.getName(), moduleLeipzigDTO.getCode());
        moduleLeipzig.setName(moduleLeipzigDTO.getName());
        moduleLeipzig.setCode(moduleCode);
        moduleLeipzigRepository.save(moduleLeipzig);
        return moduleLeipzig.getName();
    }

    /**
     * This method deletes or deactivates the {@link ModuleLeipzig} with the given {@code name}. Deactivation happens if the {@link ModuleLeipzig} has been used in a {@link Application}
     * @param name {@code name}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link ModuleLeipzig} is already deactivated
     * @return {@code String} of the operation executed
     * @see Application
     * @see ModuleLeipzig
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String deleteModuleLeipzig(String name) {
        ModuleLeipzig moduleLeipzig = getModuleLeipzigByName(name);
        if (!moduleLeipzig.getIsActive()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Leipzig is already deactivated with name: " + name);

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


    // ------- Private Methods -------

    /**
     * This method checks if a {@link ModuleLeipzig} is being used in an {@link Application} by checking the {@link ModulesConnection ModulesConnections}
     * @param moduleLeipzig {@link ModuleLeipzig}
     * @return {@code True} if the given {@link ModuleLeipzig} is being used in an {@link Application}
     * @see Application
     * @see ModulesConnection
     * @see ModuleLeipzig
     */
    private Boolean checkIfModuleIsUsedInApplications(ModuleLeipzig moduleLeipzig) {
        List<ModulesConnection> modulesConnections = modulesConnectionService.getAllModulesConnections();
        if (!modulesConnections.isEmpty()) {
            for (ModulesConnection mc : modulesConnections) {
                List<ModuleLeipzig> listOfModuleLeipzig = mc.getModulesLeipzig();
                for (ModuleLeipzig ml : listOfModuleLeipzig) {
                    if (ml.equals(moduleLeipzig)) return true;
                }
            }
        }
        return false;
    }
}
