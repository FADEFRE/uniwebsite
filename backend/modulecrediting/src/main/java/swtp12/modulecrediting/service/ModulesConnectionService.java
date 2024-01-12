package swtp12.modulecrediting.service;

import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ModuleApplicationUpdateDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigUpdateDTO;
import swtp12.modulecrediting.dto.ModulesConnectionCreateDTO;
import swtp12.modulecrediting.dto.ModulesConnectionUpdateDTO;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

import java.util.*;

@Service
public class ModulesConnectionService {
    @Autowired
    ModulesConnectionRepository modulesConnectionRepository;
    @Autowired
    ModuleApplicationService moduleApplicationService;
    @Autowired
    ModuleLeipzigService moduleLeipzigService;


    public void updateModulesConnection(List<ModulesConnectionUpdateDTO> modulesConnectionsDTO, String userRole) {
        for(ModulesConnectionUpdateDTO mc : modulesConnectionsDTO) {
            if(mc.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules Connection id must not be null");
            ModulesConnection modulesConnection = getModulesConnectionById(mc.getId());

            // handle decision block
            if(userRole.equals("study-office")) {
                if (mc.getCommentStudyOffice() != null)
                    modulesConnection.setCommentStudyOffice(mc.getCommentStudyOffice());

                if (mc.getDecisionSuggestion() != null)
                    modulesConnection.setDecisionSuggestion(mc.getDecisionSuggestion());
            }

            if(userRole.equals("pav")) {
                if (mc.getCommentDecision() != null)
                    modulesConnection.setCommentDecision(mc.getCommentDecision());

                if (mc.getDecisionFinal() != null)
                    modulesConnection.setDecisionFinal(mc.getDecisionFinal());
            }

            // handle module applications changes
            if(mc.getModuleApplications() == null)  throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cant delete all Module Applications of a Modules Connection " + mc.getId());

            List<Long> savedIdList = getIdListFromModuleConnection(modulesConnection);
            List<Long> updatedIdList = getIdListFromModuleConnectionUpdateDTO(mc);
            List<Long> deleteIdList = new ArrayList<>(savedIdList);
            deleteIdList.removeAll(updatedIdList);

            removeAllDeletedModuleApplications(deleteIdList);
            moduleApplicationService.updateModuleApplications(mc.getModuleApplications());

            // handle modules leipzig changes
            if(mc.getModulesLeipzig() == null) modulesConnection.removeAllModulesLeipzig(); // remove all module leipzig

            List<String> savedNameList = getModuleLeipzigNameFromModuleConnection(modulesConnection);
            List<String> updatedNameList = getModuleLeipzigNameFromModuleConnectionUpdateDTO(mc);
            List<String> deleteNameList = new ArrayList<>(savedNameList);
            deleteNameList.removeAll(updatedNameList);

            removeAllDeletedModulesLeipzig(modulesConnection, deleteNameList);
            moduleLeipzigService.updateModulesLeipzig(modulesConnection, mc.getModulesLeipzig());

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
    void removeAllDeletedModuleApplications(List<Long> deleteIdList) {
        for(Long id : deleteIdList) {
            moduleApplicationService.deleteModuleApplicationById(id);
        }
    }
    List<Long> getIdListFromModuleConnection(ModulesConnection modulesConnection) {
        ArrayList<Long> idList = new ArrayList<>();
        for(ModuleApplication ma : modulesConnection.getModuleApplications()) {
            idList.add(ma.getId());
        }
        return idList;
    }
    List<Long> getIdListFromModuleConnectionUpdateDTO(ModulesConnectionUpdateDTO modulesConnection) {
        ArrayList<Long> idList = new ArrayList<>();
        for(ModuleApplicationUpdateDTO ma : modulesConnection.getModuleApplications()) {
            idList.add(ma.getId());
        }
        return idList;
    }

    public List<ModulesConnection> createModulesConnections(List<ModulesConnectionCreateDTO> modulesConnectionsDTO) {
        if(modulesConnectionsDTO == null || modulesConnectionsDTO.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Modules Connections provided in the request");
        List<ModulesConnection> modulesConnections = new ArrayList<>();

        for(ModulesConnectionCreateDTO mc : modulesConnectionsDTO) {
            ModulesConnection modulesConnection = new ModulesConnection();
            modulesConnection.setCommentApplicant(mc.getCommentApplicant());

            List<ModuleApplication> moduleApplications = moduleApplicationService.createModuleApplications(mc.getModuleApplications());
            modulesConnection.setModuleApplications(moduleApplications);

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

            if(m.getDecisionFinal() == EnumModuleConnectionDecision.unedited) continue;

            if(checkSimilarityOfModulesConnection(baseModulesConnection,m)) relatedModuleConnections.add(m);
        }
        return relatedModuleConnections;
    }

    // checks if a module connection is similar, based on if any of a modulename, university pair matches with another pair.
    public boolean checkSimilarityOfModulesConnection(ModulesConnection baseModulesConnection, ModulesConnection relatedModulesConnection) {
        for(ModuleApplication maBase : baseModulesConnection.getModuleApplications()) {
            for(ModuleApplication maRel : relatedModulesConnection.getModuleApplications()) {
                int distanceModuleName = checkSimilarityOfStrings(maBase.getName(), maRel.getName());
                int distanceUniversity = checkSimilarityOfStrings(maBase.getUniversity(), maRel.getUniversity());

                if(distanceUniversity <= 5 && distanceModuleName <= 5) return true;
            }
        }
        return false;
    }

    public int checkSimilarityOfStrings(String name1, String name2) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        String name1Clean = name1.toLowerCase().replaceAll(" ", "");
        String name2Clean = name2.toLowerCase().replaceAll(" ", "");
        System.out.println(name1Clean + "   " + name2Clean);
        return levenshteinDistance.apply(name1, name1);
    }


    // GENERALL METHODS //
    public ModulesConnection getModulesConnectionById(Long id) {
        Optional<ModulesConnection> modulesConnection = modulesConnectionRepository.findById(id);
        if(modulesConnection.isPresent()) return modulesConnection.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ModulesConnection with id " + id + " not found");
    }
}
