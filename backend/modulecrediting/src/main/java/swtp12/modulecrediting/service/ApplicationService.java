package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ApplicationRepository;

// TODO: fix pdf generator

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    ModulesConnectionService modulesConnectionService;
    @Autowired
    private CourseLeipzigService courseLeipzigService;


    @Transactional
    public String updateApplication(String id, ApplicationUpdateDTO applicationDTO, String userRole) {
        Application application = getApplicationById(id);

        // check if any changes were made
        if(applicationDTO.getModulesConnections() != null) {
            modulesConnectionService.updateModulesConnection(applicationDTO.getModulesConnections(), userRole);
        }
        application.setLastEditedDate(LocalDateTime.now());

        applicationRepository.save(application);
        return id;
    }

    public Boolean updateApplicationStatusAllowed(String id) {
        Application application = getApplicationById(id);

        boolean allDecisionsSuggestionsCompleted = true;
        boolean allDecisionsFinalCompleted = true;

        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionSuggestion() == unedited) allDecisionsSuggestionsCompleted = false;
            if(m.getDecisionFinal() == unedited) allDecisionsFinalCompleted = false;
        }

        if(application.getFullStatus() == ABGESCHLOSSEN) return false;
        if(allDecisionsFinalCompleted && (application.getFullStatus() == PRÜFUNGSAUSSCHUSS || application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return true;
        if(allDecisionsSuggestionsCompleted && (application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return true;

        return false;
    }
    // FUNCTION TO UPDADTE APPLICATION STATUS ON UPDATE
    public EnumApplicationStatus updateApplicationStatus(String id) {
        Application application = getApplicationById(id);
        
        boolean noDecisionSuggestionCompleted = true;
        boolean allDecisionsSuggestionsCompleted = true;
        boolean allDecisionsFinalCompleted = true;


        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionSuggestion() == unedited)  allDecisionsSuggestionsCompleted = false;
            else noDecisionSuggestionCompleted = false;
            if(m.getDecisionFinal() == unedited) allDecisionsFinalCompleted = false;
        }

        if(allDecisionsFinalCompleted) {
            application.setFullStatus(ABGESCHLOSSEN);
            application.setDecisionDate(LocalDateTime.now());
        }
        else if(allDecisionsSuggestionsCompleted) { application.setFullStatus(PRÜFUNGSAUSSCHUSS); }
        else if(!noDecisionSuggestionCompleted) { application.setFullStatus(STUDIENBÜRO); }

        applicationRepository.save(application);
        return application.getFullStatus();
    }

    public String createApplication(ApplicationCreateDTO applicationDTO) {
        Application application = new Application(generateValidApplicationId());

        CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(applicationDTO.getCourseLeipzig());
        application.setCourseLeipzig(courseLeipzig);

        List<ModulesConnection> modulesConnections = modulesConnectionService.createModulesConnections(applicationDTO.getModulesConnections());
        application.setModulesConnections(modulesConnections);

        application = applicationRepository.save(application);
        return application.getId();
    }

    private String generateValidApplicationId() {
        String id;
        do {
            id = generateApplicationId();
        }while(applicationExists(id));

        return id;
    }

    private String generateApplicationId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    public List<Application> getAllApplciations(){
        return applicationRepository.findAll();
    }

    public Application getApplicationById(String id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            return applicationOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id: " + id + " not Found");
        }
    }

    public boolean applicationExists(String id) {
        return applicationRepository.existsById(id);
    }


}