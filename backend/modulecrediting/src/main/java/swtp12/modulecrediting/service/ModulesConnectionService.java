package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.unedited;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ModulesConnectionDTO;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

/**
 * This is a {@code Service} for {@link ModulesConnection}
 * @author Frederik Kluge
 * @author Luca Kippe
 * @see #getAllModulesConnections
 * @see #getModulesConnectionById
 * @see #getOriginalModulesConnections
 * @see #getOriginalModulesConnectionsWithFormalRejectionData
 * @see #getRelatedModulesConnections
 * @see #createModulesConnectionsWithDuplicate
 * @see #updateModulesConnection
 * @see #deleteOriginalModulesConnections
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Springboot Service</a>
 */
@Service
public class ModulesConnectionService {
    ModulesConnectionRepository modulesConnectionRepository;
    ExternalModuleService externalModuleService;
    ModuleLeipzigService moduleLeipzigService;
    
    public ModulesConnectionService(ModulesConnectionRepository modulesConnectionRepository, @Lazy ExternalModuleService externalModuleService, @Lazy ModuleLeipzigService moduleLeipzigService) {
        this.modulesConnectionRepository = modulesConnectionRepository;
        this.externalModuleService = externalModuleService;
        this.moduleLeipzigService = moduleLeipzigService;
    }

    /**
     * This method finds all {@link ModulesConnection} in the database
     * @return {@code List} of {@link ModulesConnection ModulesConnections}
     * @see ModulesConnection
     */
    public List<ModulesConnection> getAllModulesConnections() {
        return modulesConnectionRepository.findAll();
    }

    /**
     * This method gets the {@link ModulesConnection} with the given {@code id}
     * @param id of a {@link ModulesConnection} 
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if the {@link ModulesConnection} could not be found
     * @return {@link ModulesConnection} with the given {@code id}
     * @see ModulesConnection
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public ModulesConnection getModulesConnectionById(Long id) {
        return modulesConnectionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ModulesConnection with id " + id + " not found"));
    }

    /**
     * This method returns for each element in the given {@code List} of {@link ModulesConnection} their {@code OriginalModulesConnection}
     * @param modulesConnections {@code List} of {@link ModulesConnection}
     * @return {@code List} of {@link ModulesConnection} which represent the {@code OriginalModulesConnections}
     * @see ModulesConnection
     */
    public List<ModulesConnection> getOriginalModulesConnections(List<ModulesConnection> modulesConnections) {
        List<ModulesConnection> modulesConnectionsOriginal = new ArrayList<>();
        for(ModulesConnection modulesConnection : modulesConnections) {
            modulesConnectionsOriginal.add(modulesConnection.getModulesConnectionOriginal());
        }
        return modulesConnectionsOriginal;
    }

    /**
     * This method returns for each element in the given {@code List} of {@link ModulesConnection} their {@code OriginalModulesConnection} and adds the {@code FormalRejection(Comment)}
     * @param editModulesConnections {@code List} of {@link ModulesConnection}
     * @return {@code List} of {@link ModulesConnection} which represent the {@code OriginalModulesConnections} with the {@code FormalRejection(Comment)}
     * @see ModulesConnection
     */
    public List<ModulesConnection> getOriginalModulesConnectionsWithFormalRejectionData(List<ModulesConnection> editModulesConnections) {
        List<ModulesConnection> modulesConnectionsOriginal = new ArrayList<>();
        for(ModulesConnection editModulesConnection : editModulesConnections) {
            ModulesConnection modulesConnectionOriginal = editModulesConnection.getModulesConnectionOriginal();
            modulesConnectionOriginal.setFormalRejection(editModulesConnection.getFormalRejection());
            modulesConnectionOriginal.setFormalRejectionComment(editModulesConnection.getFormalRejectionComment());
            modulesConnectionsOriginal.add(modulesConnectionOriginal);
        }
        return modulesConnectionsOriginal;
    }

    /**
     * This method gets the {@code RelatedModulesConnections} of a {@link ModulesConnection} and filters them
     * @param id of a {@link ModulesConnection} 
     * @return {@code List} of {@link ModulesConnection} which represent the {@code RelatedModulesConnections}
     * @see ModulesConnection
     */
    public List<ModulesConnection> getRelatedModulesConnections(Long id) {
        ModulesConnection baseModulesConnection = getModulesConnectionById(id);
        List<ModulesConnection> allModulesConnections = modulesConnectionRepository.findAll();
        List<ModulesConnection> relatedModuleConnections = new ArrayList<>();

        for(ModulesConnection m : allModulesConnections) {
            // skip if its the same modules connection
            if(m.getId() == baseModulesConnection.getId()) continue;

            // skip original modules connections
            if(m.getIsOriginalModulesConnection()) continue;

            // only completed applications
            if(m.getApplication().getFullStatus() != EnumApplicationStatus.ABGESCHLOSSEN) continue;

            if(m.getDecisionFinal() == unedited) continue;

            if(checkSimilarityOfModulesConnection(baseModulesConnection,m)) relatedModuleConnections.add(m);
        }
        return filterRelevantRelatedModulesConnections(relatedModuleConnections);
    }

    /**
     * This method creates all {@link ModulesConnection} defined in {@code modulesConnectionDTOs} and a duplicate of each as {@code OriginalModulesConnection}
     * @param modulesConnectionDTOs {@code List} of {@link ModulesConnectionDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code List} of {@link ModulesConnectionDTO} is {@code null} or {@code empty}
     * @return {@code List} of all created {@link ModulesConnection} (without the {@code OriginalModulesConnection})
     * @see ModulesConnection
     * @see ModulesConnectionDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public List<ModulesConnection> createModulesConnectionsWithDuplicate(List<ModulesConnectionDTO> modulesConnectionDTOs) {
        if(modulesConnectionDTOs == null || modulesConnectionDTOs.size() == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Modules Connections provided in the request");
        
        List<ModulesConnection> modulesConnections = new ArrayList<>();

        for(ModulesConnectionDTO modulesConnectionDTO : modulesConnectionDTOs) {
            ModulesConnection modulesConnection = createModulesConnection(modulesConnectionDTO);
            ModulesConnection modulesConnectionOriginal = createModulesConnection(modulesConnectionDTO);
            modulesConnectionOriginal.setIsOriginalModulesConnection(true);

            modulesConnection.setModulesConnectionOriginal(modulesConnectionOriginal);

            modulesConnections.add(modulesConnection);
        }
        return modulesConnections;
    }

    /**
     * This method updates all {@link ModulesConnection} defined in {@code modulesConnectionDTOs} and depending on the {@code userRole}
     * @param modulesConnectionDTOs {@code List} of {@link ModulesConnectionDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code List} of {@link ModulesConnectionDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code id} of any of the {@link ModulesConnectionDTO} in the given {@code List} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.CONFLICT: 409} if the {@code ExternalModules} of any of the {@link ModulesConnectionDTO} in the given {@code List} is {@code null}
     * @see ModulesConnection
     * @see ModulesConnectionDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public void updateModulesConnection(List<ModulesConnectionDTO> modulesConnectionDTOs, String userRole) {
        if(modulesConnectionDTOs == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules Connections must not be null");

        for(ModulesConnectionDTO mcuDTO : modulesConnectionDTOs) {
            if(mcuDTO.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules Connection id must not be null");
            if(mcuDTO.getExternalModules() == null) throw new ResponseStatusException(HttpStatus.CONFLICT, "You cant delete all External Modules of a Modules Connection " + mcuDTO.getId());

            ModulesConnection modulesConnection = getModulesConnectionById(mcuDTO.getId());

            // handle study office decision
            if(userRole.equals("study-office")) {
                if (mcuDTO.getCommentStudyOffice() != null) { modulesConnection.setCommentStudyOffice(mcuDTO.getCommentStudyOffice()); }
                if (mcuDTO.getDecisionSuggestion() != null) { modulesConnection.setDecisionSuggestion(mcuDTO.getDecisionSuggestion()); }
                // allow only study-office to reject as formal rejection
                if(mcuDTO.getFormalRejection() != null) { modulesConnection.setFormalRejection(mcuDTO.getFormalRejection()); }
                if(mcuDTO.getFormalRejectionComment() != null) { modulesConnection.setFormalRejectionComment(mcuDTO.getFormalRejectionComment()); }
            }
            // handle chairman decision
            if(userRole.equals("chairman")) {
                if (mcuDTO.getCommentDecision() != null) { modulesConnection.setCommentDecision(mcuDTO.getCommentDecision()); }
                if (mcuDTO.getDecisionFinal() != null) { modulesConnection.setDecisionFinal(mcuDTO.getDecisionFinal()); }
            }

            // check difference saved external modules <-> external modules sent in dto => remove deleted external modules
            removeAllDeletedExternalModules(modulesConnection, mcuDTO);

            // update external modules data
            externalModuleService.updateExternalModules(mcuDTO.getExternalModules());

            // handle modules leipzig changes
            if(mcuDTO.getModulesLeipzig() == null) {
                modulesConnection.removeAllModulesLeipzig(); 
            }
            else {
                // check difference saved modules leipzig <-> modules leipzig sent in dto => remove relation deleted module leipzig
                removeAllDeletedModulesLeipzig(modulesConnection, mcuDTO);
                // update relation (create new, ignoring old)
                moduleLeipzigService.updateRelationModulesConnectionToModulesLeipzig(modulesConnection, mcuDTO.getModulesLeipzig());
            }

            // modulesConnection will be saved in application service due to cascade all
        }
    }

    /**
     * This method deletes all {@code OriginalModulesConnection} of all given {@code modulesConnections}
     * @param modulesConnections {@code List} of {@link ModulesConnection}
     * @see ModulesConnection
     */
    public void deleteOriginalModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection modulesConnection : modulesConnections) {
            modulesConnection.setModulesConnectionOriginal(null);
            modulesConnectionRepository.save(modulesConnection);
        }
    }


    // ------- Private Methods -------

    /**
     * This method creates a {@link ModulesConnection}
     * @param modulesConnectionDTO {@link ModulesConnectionDTO}
     * @return the created {@link ModulesConnection}
     * @see ModulesConnection
     * @see ModulesConnectionDTO
     */
    private ModulesConnection createModulesConnection(ModulesConnectionDTO modulesConnectionDTO) {
        ModulesConnection modulesConnection = new ModulesConnection();

        // create external modules
        List<ExternalModule> externalModules = externalModuleService.createExternalModules(modulesConnectionDTO.getExternalModules());
        modulesConnection.setExternalModules(externalModules);

        // create modules lepizig relation
        if(modulesConnectionDTO.getModulesLeipzig() != null) { // no modules leipzig sent in dto
            List<ModuleLeipzig> modulesLeipzig = moduleLeipzigService.getModulesLeipzigByNamesFromDTO(modulesConnectionDTO.getModulesLeipzig());
            modulesConnection.setModulesLeipzig(modulesLeipzig);
        }

        // create set comment applicant
        modulesConnection.setCommentApplicant(modulesConnectionDTO.getCommentApplicant());

        return modulesConnection;
    }

    /**
     * This methods removes all {@link ModuleLeipzig} not definied in the {@link ModulesConnectionDTO} from the given {@link ModulesConnection}
     * @param modulesConnection {@link ModulesConnection}
     * @param mcuDTO {@link ModulesConnectionDTO}
     * @see ModulesConnection
     * @see ModulesConnectionDTO
     * @see ModuleLeipzig
     */
    private void removeAllDeletedModulesLeipzig(ModulesConnection modulesConnection, ModulesConnectionDTO mcuDTO) {
        List<String> savedNameList = new ArrayList<>();
        List<String> updatedNameList = new ArrayList<>();
        List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();

        modulesConnection.getModulesLeipzig().forEach(mL-> savedNameList.add(mL.getName()));
        mcuDTO.getModulesLeipzig().forEach(mLDTO -> updatedNameList.add(mLDTO.getName()));

        List<String> deleteNameList = new ArrayList<>(savedNameList);
        deleteNameList.removeAll(updatedNameList);
        deleteNameList.forEach(name -> modulesLeipzig.add(moduleLeipzigService.getModuleLeipzigByName(name)));

        modulesConnection.removeModulesLeipzig(modulesLeipzig);
    }

    /**
     * This methods removes all {@link ExternalModule} not definied in the {@link ModulesConnectionDTO} from the given {@link ModulesConnection}
     * @param modulesConnection {@link ModulesConnection}
     * @param mcuDTO {@link ModulesConnectionDTO}    
     * @see ExternalModule
     * @see ModulesConnection
     * @see ModulesConnectionDTO
     */
    private void removeAllDeletedExternalModules(ModulesConnection modulesConnection, ModulesConnectionDTO mcuDTO) {
        List<Long> savedIdList = new ArrayList<>();
        List<Long> updatedIdList = new ArrayList<>();

        modulesConnection.getExternalModules().forEach(eM -> savedIdList.add(eM.getId()));
        mcuDTO.getExternalModules().forEach(eMDTO -> updatedIdList.add(eMDTO.getId()));

        List<Long> deleteIdList = new ArrayList<>(savedIdList);
        deleteIdList.removeAll(updatedIdList);
        deleteIdList.forEach(id -> externalModuleService.deleteExternalModuleById(id));
    }

    /**
     * This method checks if two {@link ModulesConnection} are related by comparing their {@link ExternalModule ExternalModules}
     * @param baseModulesConnection {@link ModulesConnection}
     * @param relatedModulesConnection {@link ModulesConnection}
     * @return {@code True} when the two given {@link ModulesConnection ModulesConnections} are deemed related
     * @see ExternalModule
     * @see ModulesConnection
     */
    private boolean checkSimilarityOfModulesConnection(ModulesConnection baseModulesConnection, ModulesConnection relatedModulesConnection) {
        for(ExternalModule emBase : baseModulesConnection.getExternalModules()) {
            for(ExternalModule emRel : relatedModulesConnection.getExternalModules()) {
                int distanceModuleName = checkSimilarityOfStrings(emBase.getName(), emRel.getName());
                int distanceUniversity = checkSimilarityOfStrings(emBase.getUniversity(), emRel.getUniversity());

                if(distanceUniversity <= 5 && distanceModuleName <= 5) return true;
            }
        }
        return false;
    }
    
    /**
     * This method gets the {@code LevenshteinDistance} of two {@code String}
     * @param name1 {@code String}
     * @param name2 {@code String}
     * @return {@code int} that represents the {@code LevenshteinDistance} of the two given {@code String}
     * @see <a href="https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/similarity/LevenshteinDistance.html">Apache LevenshteinDistance</a> 
     */
    private int checkSimilarityOfStrings(String name1, String name2) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        String name1Clean = name1.toLowerCase().replaceAll(" ", "");
        String name2Clean = name2.toLowerCase().replaceAll(" ", "");
        return levenshteinDistance.apply(name1Clean, name2Clean);
    }
    
    /**
     * This method filters the given {@code List} of {@link ModulesConnection} to return the latest {@code 5}
     * @param allRelatedModulesConnections {@code List} of {@link ModulesConnection}
     * @return the filtered {@code List} of {@link ModulesConnection}
     * @see ModulesConnection
     */
    private List<ModulesConnection> filterRelevantRelatedModulesConnections(List<ModulesConnection> allRelatedModulesConnections) {
        List<ModulesConnection> filteredRelatedModCons = new ArrayList<>();
        for (ModulesConnection modulesConnection : allRelatedModulesConnections) {
            if (filteredRelatedModCons.size() >= 5) {
                ModulesConnection oldestConnection = modulesConnection;
                for (ModulesConnection filteredConnection : filteredRelatedModCons) {
                    if (filteredConnection.getApplication().getDecisionDate().isBefore(oldestConnection.getApplication().getDecisionDate())) {
                        oldestConnection = filteredConnection;
                    }
                }
                if (!oldestConnection.equals(modulesConnection)) {
                    filteredRelatedModCons.remove(oldestConnection);
                    filteredRelatedModCons.add(modulesConnection);
                }
            }
            else {
                filteredRelatedModCons.add(modulesConnection);
            }
        }
        return filteredRelatedModCons;
    }

}
