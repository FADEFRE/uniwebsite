package swtp12.modulecrediting.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ExternalModuleUpdateDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigUpdateDTO;
import swtp12.modulecrediting.dto.ModulesConnectionCreateDTO;
import swtp12.modulecrediting.dto.ModulesConnectionUpdateDTO;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

import java.util.*;

import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.asExamCertificate;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.unedited;

@Service
public class ModulesConnectionService {
    @Autowired
    ModulesConnectionRepository modulesConnectionRepository;
    @Autowired
    ExternalModuleService externalModuleService;
    @Autowired
    ModuleLeipzigService moduleLeipzigService;


    public void updateModulesConnection(List<ModulesConnectionUpdateDTO> modulesConnectionsDTO, String userRole) {
        for(ModulesConnectionUpdateDTO mcuDTO : modulesConnectionsDTO) {
            if(mcuDTO.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules Connection id must not be null");
            ModulesConnection modulesConnection = getModulesConnectionById(mcuDTO.getId());

            // handle decision block
            if(userRole.equals("study-office")) {
                if (mcuDTO.getCommentStudyOffice() != null)
                    modulesConnection.setCommentStudyOffice(mcuDTO.getCommentStudyOffice());

                if (mcuDTO.getDecisionSuggestion() != null)
                    modulesConnection.setDecisionSuggestion(mcuDTO.getDecisionSuggestion());

                if(mcuDTO.getFormalRejection() != null)
                    modulesConnection.setFormalRejection(mcuDTO.getFormalRejection());

                if(mcuDTO.getFormalRejectionComment() != null)
                    modulesConnection.setFormalRejectionComment(mcuDTO.getFormalRejectionComment());
            }

            if(userRole.equals("chairman")) {
                if (mcuDTO.getCommentDecision() != null)
                    modulesConnection.setCommentDecision(mcuDTO.getCommentDecision());

                if (mcuDTO.getDecisionFinal() != null)
                    modulesConnection.setDecisionFinal(mcuDTO.getDecisionFinal());
            }

            if(userRole.equals("standard")) {
                // when user corrects application, the status of connection goes back to non rejecting
                modulesConnection.setFormalRejection(false);
                modulesConnection.setFormalRejectionComment("");

                if(mcuDTO.getCommentApplicant() != null)
                    modulesConnection.setCommentApplicant(mcuDTO.getCommentApplicant());
            }


            // handle module applications changes
            if(mcuDTO.getExternalModules() == null)  throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cant delete all External Modules of a Modules Connection " + mcuDTO.getId());

            List<Long> savedIdList = getIdListFromModuleConnection(modulesConnection);
            List<Long> updatedIdList = getIdListFromModuleConnectionUpdateDTO(mcuDTO);
            List<Long> deleteIdList = new ArrayList<>(savedIdList);
            deleteIdList.removeAll(updatedIdList);

            removeAllDeletedExternalModules(deleteIdList);
            externalModuleService.updateExternalModules(mcuDTO.getExternalModules(), userRole);

            // handle modules leipzig changes
            if(mcuDTO.getModulesLeipzig() == null) modulesConnection.removeAllModulesLeipzig(); // remove all module leipzig
            else{
                List<String> savedNameList = getModuleLeipzigNameFromModuleConnection(modulesConnection);
                List<String> updatedNameList = getModuleLeipzigNameFromModuleConnectionUpdateDTO(mcuDTO);
                List<String> deleteNameList = new ArrayList<>(savedNameList);
                deleteNameList.removeAll(updatedNameList);
                removeAllDeletedModulesLeipzig(modulesConnection, deleteNameList);
                moduleLeipzigService.updateModulesLeipzig(modulesConnection, mcuDTO.getModulesLeipzig());
            }

            // modulesConnection will be saved in application service due to cascade all
        }
    }

    // modules leipzig helper methods
    void removeAllDeletedModulesLeipzig(ModulesConnection modulesConnection, List<String> deleteIdList) {
        ArrayList<ModuleLeipzig> modulesLeipzig = new ArrayList<>();
        for(String name : deleteIdList) {
            modulesLeipzig.add(moduleLeipzigService.getModuleLeipzigByName(name));
        }
        modulesConnection.removeModulesLeipzig(modulesLeipzig);
    }
    List<String> getModuleLeipzigNameFromModuleConnection(ModulesConnection modulesConnection) {
        ArrayList<String> nameList = new ArrayList<>();
        for(ModuleLeipzig ml : modulesConnection.getModulesLeipzig()) {
            nameList.add(ml.getName());
        }
        return nameList;
    }
    List<String> getModuleLeipzigNameFromModuleConnectionUpdateDTO(ModulesConnectionUpdateDTO modulesConnection) {
        ArrayList<String> nameList = new ArrayList<>();
        for(ModuleLeipzigUpdateDTO ml : modulesConnection.getModulesLeipzig()) {
            nameList.add(ml.getName());
        }
        return nameList;
    }

    // module applications helper methos (external modules)
    void removeAllDeletedExternalModules(List<Long> deleteIdList) {
        for(Long id : deleteIdList) {
            externalModuleService.deleteExternalModuleById(id);
        }
    }
    List<Long> getIdListFromModuleConnection(ModulesConnection modulesConnection) {
        ArrayList<Long> idList = new ArrayList<>();
        for(ExternalModule eM : modulesConnection.getExternalModules()) {
            idList.add(eM.getId());
        }
        return idList;
    }
    List<Long> getIdListFromModuleConnectionUpdateDTO(ModulesConnectionUpdateDTO modulesConnection) {
        ArrayList<Long> idList = new ArrayList<>();
        for(ExternalModuleUpdateDTO eM : modulesConnection.getExternalModules()) {
            idList.add(eM.getId());
        }
        return idList;
    }

    public void removeAllDecisions(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.setDecisionSuggestion(unedited);
            mc.setCommentStudyOffice("");
            mc.setDecisionFinal(unedited);
            mc.setCommentDecision("");
        }
    }

    public List<ModulesConnection> createModulesConnections(List<ModulesConnectionCreateDTO> modulesConnectionsDTO) {
        if(modulesConnectionsDTO == null || modulesConnectionsDTO.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Modules Connections provided in the request");
        List<ModulesConnection> modulesConnections = new ArrayList<>();

        for(ModulesConnectionCreateDTO mc : modulesConnectionsDTO) {
            ModulesConnection modulesConnection = new ModulesConnection();
            modulesConnection.setCommentApplicant(mc.getCommentApplicant());

            List<ExternalModule> externalModules = externalModuleService.createExternalModules(mc.getExternalModules());
            modulesConnection.setExternalModules(externalModules);

            if(mc.getModulesLeipzig() != null) { // no modules leipzig sent
                List<ModuleLeipzig> modulesLeipzig = moduleLeipzigService.getModulesLeipzigByNames(mc.getModulesLeipzig());
                modulesConnection.setModulesLeipzig(modulesLeipzig);
            }

            modulesConnections.add(modulesConnection);
        }

        return modulesConnections;
    }




    // METHODS FOR RELATED MODULES //

    public ArrayList<ModulesConnection> getRelatedModulesConnections(Long id) {
        ModulesConnection baseModulesConnection = getModulesConnectionById(id);
        List<ModulesConnection> allModulesConnections = modulesConnectionRepository.findAll();
        ArrayList<ModulesConnection> relatedModuleConnections = new ArrayList<>();

        for(ModulesConnection m : allModulesConnections) {
            if(m.getId() == baseModulesConnection.getId()) continue;

            if(m.getDecisionFinal() == unedited || m.getDecisionFinal() == asExamCertificate) continue;

            if(checkSimilarityOfModulesConnection(baseModulesConnection,m)) relatedModuleConnections.add(m);
        }
        return relatedModuleConnections;
    }

    // checks if a module connection is similar, based on if any of a modulename, university pair matches with another pair.
    public boolean checkSimilarityOfModulesConnection(ModulesConnection baseModulesConnection, ModulesConnection relatedModulesConnection) {
        for(ExternalModule emBase : baseModulesConnection.getExternalModules()) {
            for(ExternalModule emRel : relatedModulesConnection.getExternalModules()) {
                int distanceModuleName = checkSimilarityOfStrings(emBase.getName(), emRel.getName());
                int distanceUniversity = checkSimilarityOfStrings(emBase.getUniversity(), emRel.getUniversity());

                if(distanceUniversity <= 5 && distanceModuleName <= 5) return true;
            }
        }
        return false;
    }

    public int checkSimilarityOfStrings(String name1, String name2) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        String name1Clean = name1.toLowerCase().replaceAll(" ", "");
        String name2Clean = name2.toLowerCase().replaceAll(" ", "");
        return levenshteinDistance.apply(name1Clean, name2Clean);
    }


    // GENERALL METHODS //
    public ModulesConnection getModulesConnectionById(Long id) {
        Optional<ModulesConnection> modulesConnection = modulesConnectionRepository.findById(id);
        if(modulesConnection.isPresent()) return modulesConnection.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ModulesConnection with id " + id + " not found");
    }
}
