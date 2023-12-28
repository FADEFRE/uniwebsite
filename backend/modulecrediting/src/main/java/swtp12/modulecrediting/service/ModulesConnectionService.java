package swtp12.modulecrediting.service;

import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ModulesConnectionCreateDTO;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ModulesConnectionService {
    @Autowired
    ModulesConnectionRepository modulesConnectionRepository;
    @Autowired
    ModuleApplicationService moduleApplicationService;
    @Autowired
    ModuleLeipzigService moduleLeipzigService;


    public List<ModulesConnection> createModulesConnections(List<ModulesConnectionCreateDTO> modulesConnectionsDTO) {
        if(modulesConnectionsDTO == null || modulesConnectionsDTO.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Modules Connections provided in the request");
        List<ModulesConnection> modulesConnections = new ArrayList<>();

        for(ModulesConnectionCreateDTO mc : modulesConnectionsDTO) {
            ModulesConnection modulesConnection = new ModulesConnection();
            modulesConnection.setCommentApplicant(mc.getCommentApplicant());

            List<ModuleApplication> moduleApplications = moduleApplicationService.createModuleApplications(mc.getModuleApplications());
            modulesConnection.setModuleApplications(moduleApplications);

            List<ModuleLeipzig> modulesLeipzig = moduleLeipzigService.getModulesLeipzigByNames(mc.getModulesLeipzig());
            modulesConnection.setModulesLeipzig(modulesLeipzig);

            modulesConnections.add(modulesConnection);
        }

        return modulesConnections;
    }




    // DOWN FROM HERE: METHODS FOR RELATED MODULES //

    public List<ModulesConnection> getRelatedModulesConnections(Long id) {
        ModulesConnection baseModulesConnection = getModulesConnectionById(id);
        List<ModulesConnection> allModulesConnections = modulesConnectionRepository.findAll();
        ArrayList<ModulesConnection> relatedModuleConnections = new ArrayList<>();

        for(ModulesConnection m : allModulesConnections) {
            if(m.getId() == baseModulesConnection.getId()) continue;

            if(m.getDecisionFinal() == EnumModuleConnectionDecision.UNBEARBEITET) continue;

           // if(checkSimilarity(baseModulesConnection,m)) relatedModuleConnections.add(m);
        }
        return relatedModuleConnections;
    }

    public ModulesConnection getModulesConnectionById(Long id) {
        Optional<ModulesConnection> modulesConnection = modulesConnectionRepository.findById(id);
        if(modulesConnection.isPresent()) return modulesConnection.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ModulesConnection with id " + id + " not found");
    }

    /*
    public boolean checkSimilarity(ModulesConnection baseModulesConnection, ModulesConnection relatedModulesConnection) {
        String baseUniversity = baseModulesConnection.getModuleApplication().getUniversity();
        String baseModuleName = baseModulesConnection.getModuleApplication().getName();

        String relatedUniversity = relatedModulesConnection.getModuleApplication().getUniversity();
        String relatedModuleName = relatedModulesConnection.getModuleApplication().getName();

        // TODO convert Strings no spaces, all lower case etc.? use LevenSteinDetalied Distance for universiry

        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance();

        int distanceUniversity = levenshteinDistance.apply(baseUniversity, relatedUniversity);
        int distanceModuleName = levenshteinDistance.apply(baseModuleName, relatedModuleName);

        System.out.println(distanceUniversity + " " + distanceModuleName + " ");

        if(distanceUniversity <= 5 && distanceModuleName <= 5) return true;
        return false;
    }*/
}
