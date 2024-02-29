package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.unedited;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ExternalModuleDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.dto.ModulesConnectionDTO;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

@Service
public class ModulesConnectionService {
    @Autowired
    ModulesConnectionRepository modulesConnectionRepository;
    ExternalModuleService externalModuleService;
    ModuleLeipzigService moduleLeipzigService;
    
    public ModulesConnectionService(@Lazy ExternalModuleService externalModuleService, @Lazy ModuleLeipzigService moduleLeipzigService) {
        this.externalModuleService = externalModuleService;
        this.moduleLeipzigService = moduleLeipzigService;
    }

    public List<ModulesConnection> getAllModulesConnections() {
        return modulesConnectionRepository.findAll();
    }

    public ModulesConnection getModulesConnectionById(Long id) {
        Optional<ModulesConnection> modulesConnection = modulesConnectionRepository.findById(id);
        if(modulesConnection.isPresent()) return modulesConnection.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ModulesConnection with id " + id + " not found");
    }

    public List<ModulesConnection> createModulesConnectionsWithDuplicate(List<ModulesConnectionDTO> modulesConnectionsDTO) {
        if(modulesConnectionsDTO == null || modulesConnectionsDTO.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Modules Connections provided in the request");

        List<ModulesConnection> modulesConnections = new ArrayList<>();

        for(ModulesConnectionDTO modulesConnectionDTO : modulesConnectionsDTO) {
            ModulesConnection modulesConnection = createModulesConnection(modulesConnectionDTO);
            ModulesConnection modulesConnectionOriginal = createModulesConnection(modulesConnectionDTO);
            modulesConnectionOriginal.setIsOriginalModulesConnection(true);

            modulesConnection.setModulesConnectionOriginal(modulesConnectionOriginal);

            modulesConnections.add(modulesConnection);
        }
        return modulesConnections;
    }

    public void updateModulesConnection(List<ModulesConnectionDTO> modulesConnectionsDTO, String userRole) {
        if(modulesConnectionsDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules Connections must not be null");

        for(ModulesConnectionDTO mcuDTO : modulesConnectionsDTO) {
            if(mcuDTO.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules Connection id must not be null");

            ModulesConnection modulesConnection = getModulesConnectionById(mcuDTO.getId());

            // handle study office decision
            if(userRole.equals("study-office")) {
                if (mcuDTO.getCommentStudyOffice() != null)
                    modulesConnection.setCommentStudyOffice(mcuDTO.getCommentStudyOffice());

                if (mcuDTO.getDecisionSuggestion() != null)
                    modulesConnection.setDecisionSuggestion(mcuDTO.getDecisionSuggestion());

                // allow only study-office to reject as formal rejection
                if(mcuDTO.getFormalRejection() != null)
                    modulesConnection.setFormalRejection(mcuDTO.getFormalRejection());

                if(mcuDTO.getFormalRejectionComment() != null)
                    modulesConnection.setFormalRejectionComment(mcuDTO.getFormalRejectionComment());
            }

            // handle chairman decision
            if(userRole.equals("chairman")) {
                if (mcuDTO.getCommentDecision() != null)
                    modulesConnection.setCommentDecision(mcuDTO.getCommentDecision());

                if (mcuDTO.getDecisionFinal() != null)
                    modulesConnection.setDecisionFinal(mcuDTO.getDecisionFinal());
            }

            // handle module applications changes
            if(mcuDTO.getExternalModules() == null)  throw new ResponseStatusException(HttpStatus.CONFLICT, "You cant delete all External Modules of a Modules Connection " + mcuDTO.getId());

            // check difference saved external modules <-> external modules sent in dto => remove deleted external modules
            List<Long> savedIdList = getIdListFromModuleConnection(modulesConnection);
            List<Long> updatedIdList = getIdListFromModuleConnectionUpdateDTO(mcuDTO);
            List<Long> deleteIdList = new ArrayList<>(savedIdList);
            deleteIdList.removeAll(updatedIdList);
            removeAllDeletedExternalModules(deleteIdList);
            // update external modules data
            externalModuleService.updateExternalModules(mcuDTO.getExternalModules());

            // handle modules leipzig changes
            if(mcuDTO.getModulesLeipzig() == null) modulesConnection.removeAllModulesLeipzig(); // remove all module leipzig
            else{
                // check difference saved modules leipzig <-> modules leipzig sent in dto => remove relation deleted module leipzig
                List<String> savedNameList = getModuleLeipzigNameFromModuleConnection(modulesConnection);
                List<String> updatedNameList = getModuleLeipzigNameFromModuleConnectionUpdateDTO(mcuDTO);
                List<String> deleteNameList = new ArrayList<>(savedNameList);
                deleteNameList.removeAll(updatedNameList);
                removeAllDeletedModulesLeipzig(modulesConnection, deleteNameList);
                // update relation (create new, ignoring old)
                moduleLeipzigService.updateRelationModulesConnectionToModulesLeipzig(modulesConnection, mcuDTO.getModulesLeipzig());
            }

            // modulesConnection will be saved in application service due to cascade all
        }
    }

    public void deleteOriginalModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection modulesConnection : modulesConnections) {
            modulesConnection.setModulesConnectionOriginal(null);
            modulesConnectionRepository.save(modulesConnection);
        }
    }

    public List<ModulesConnection> getOriginalModulesConnections(List<ModulesConnection> modulesConnections) {
        ArrayList<ModulesConnection> modulesConnectionsOriginal = new ArrayList<>();

        for(ModulesConnection modulesConnection : modulesConnections) {
            modulesConnectionsOriginal.add(modulesConnection.getModulesConnectionOriginal());
        }
        return modulesConnectionsOriginal;
    }

    public List<ModulesConnection> getOriginalModulesConnectionsWithFormalRejectionData(List<ModulesConnection> editModulesConnections) {
        ArrayList<ModulesConnection> modulesConnectionsOriginal = new ArrayList<>();

        for(ModulesConnection editModulesConnection : editModulesConnections) {

            ModulesConnection modulesConnectionOriginal = editModulesConnection.getModulesConnectionOriginal();
            // adding formal rejection data
            modulesConnectionOriginal.setFormalRejection(editModulesConnection.getFormalRejection());
            modulesConnectionOriginal.setFormalRejectionComment(editModulesConnection.getFormalRejectionComment());

            modulesConnectionsOriginal.add(modulesConnectionOriginal);
        }
        return modulesConnectionsOriginal;
    }

    public List<ModulesConnection> getRelatedModulesConnections(Long id) {
        ModulesConnection baseModulesConnection = getModulesConnectionById(id);
        List<ModulesConnection> allModulesConnections = modulesConnectionRepository.findAll();
        List<ModulesConnection> relatedModuleConnections = new ArrayList<>();

        for(ModulesConnection m : allModulesConnections) {
            // skip if its the same modules connection
            if(m.getId() == baseModulesConnection.getId()) continue;

            // skip original modules connections
            if(m.getIsOriginalModulesConnection()) continue;

            // only abgeschlossene applications
            if(m.getApplication().getFullStatus() != EnumApplicationStatus.ABGESCHLOSSEN) continue;

            if(m.getDecisionFinal() == unedited) continue;

            if(checkSimilarityOfModulesConnection(baseModulesConnection,m)) relatedModuleConnections.add(m);
        }
        return filterRelevantRelatedModulesConnections(relatedModuleConnections, id);
    }


    private ModulesConnection createModulesConnection(ModulesConnectionDTO modulesConnectionDTO) {
        ModulesConnection modulesConnection = new ModulesConnection();

        // create external modules
        List<ExternalModule> externalModules = externalModuleService.createExternalModules(modulesConnectionDTO.getExternalModules());
        modulesConnection.setExternalModules(externalModules);

        // create modules lepizig relation
        if(modulesConnectionDTO.getModulesLeipzig() != null) { // no modules leipzig sent in dto
            List<ModuleLeipzig> modulesLeipzig = moduleLeipzigService.getModulesLeipzigByNames(modulesConnectionDTO.getModulesLeipzig());
            modulesConnection.setModulesLeipzig(modulesLeipzig);
        }

        // create set comment applicant
        modulesConnection.setCommentApplicant(modulesConnectionDTO.getCommentApplicant());

        return modulesConnection;
    }

    // modules leipzig helper methods for update application
    private void removeAllDeletedModulesLeipzig(ModulesConnection modulesConnection, List<String> deleteIdList) {
        ArrayList<ModuleLeipzig> modulesLeipzig = new ArrayList<>();
        for(String name : deleteIdList) {
            modulesLeipzig.add(moduleLeipzigService.getModuleLeipzigByName(name));
        }
        modulesConnection.removeModulesLeipzig(modulesLeipzig);
    }
    private List<String> getModuleLeipzigNameFromModuleConnection(ModulesConnection modulesConnection) {
        ArrayList<String> nameList = new ArrayList<>();
        for(ModuleLeipzig ml : modulesConnection.getModulesLeipzig()) {
            nameList.add(ml.getName());
        }
        return nameList;
    }
    private List<String> getModuleLeipzigNameFromModuleConnectionUpdateDTO(ModulesConnectionDTO modulesConnection) {
        ArrayList<String> nameList = new ArrayList<>();
        for(ModuleLeipzigDTO ml : modulesConnection.getModulesLeipzig()) {
            nameList.add(ml.getName());
        }
        return nameList;
    }

    // external modules helper methods for update application
    private void removeAllDeletedExternalModules(List<Long> deleteIdList) {
        for(Long id : deleteIdList) {
            externalModuleService.deleteExternalModuleById(id);
        }
    }
    private List<Long> getIdListFromModuleConnection(ModulesConnection modulesConnection) {
        ArrayList<Long> idList = new ArrayList<>();
        for(ExternalModule eM : modulesConnection.getExternalModules()) {
            idList.add(eM.getId());
        }
        return idList;
    }
    private List<Long> getIdListFromModuleConnectionUpdateDTO(ModulesConnectionDTO modulesConnection) {
        ArrayList<Long> idList = new ArrayList<>();
        for(ExternalModuleDTO eM : modulesConnection.getExternalModules()) {
            idList.add(eM.getId());
        }
        return idList;
    }

    // helper methods for related modules
    // checks if a module connection is similar, based on if any of a modulename, university pair matches with another pair.
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
    private int checkSimilarityOfStrings(String name1, String name2) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        String name1Clean = name1.toLowerCase().replaceAll(" ", "");
        String name2Clean = name2.toLowerCase().replaceAll(" ", "");
        return levenshteinDistance.apply(name1Clean, name2Clean);
    }
    private List<ModulesConnection> filterRelevantRelatedModulesConnections(List<ModulesConnection> allRelatedModulesConnections, Long id) {
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
