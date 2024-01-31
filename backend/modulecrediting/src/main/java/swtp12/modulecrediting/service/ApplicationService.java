package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;
import static swtp12.modulecrediting.dto.EnumStatusChangeAllowed.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import swtp12.modulecrediting.dto.*;
import swtp12.modulecrediting.model.*;
import swtp12.modulecrediting.repository.ApplicationRepository;


@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    ModulesConnectionService modulesConnectionService;
    @Autowired
    private CourseLeipzigService courseLeipzigService;

    public String createApplication(ApplicationDTO applicationDTO) {
        String id = generateValidApplicationId();
        Application application = new Application(id);

        CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(applicationDTO.getCourseLeipzig());
        application.setCourseLeipzig(courseLeipzig);

        List<ModulesConnection> modulesConnections = modulesConnectionService.createModulesConnectionsWithDuplicate(applicationDTO.getModulesConnections());
        application.setModulesConnections(modulesConnections);

        application = applicationRepository.save(application);
        return application.getId();
    }

    @Transactional
    public String updateApplicationAfterFormalRejection(String id, ApplicationDTO applicationDTO) {
        Application application = getApplicationById(id);

        if(application.getFullStatus() != FORMFEHLER)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only FORMFEHLER applications can be updated by student");

        application.setFullStatus(STUDIENBÜRO);
        application.setLastEditedDate(LocalDateTime.now());

        // delete old modules connections
        application.removeAllModulesConnections();

        // create new modules connections
        List<ModulesConnection> modulesConnections = modulesConnectionService.createModulesConnectionsWithDuplicate(applicationDTO.getModulesConnections());
        application.addModulesConnections(modulesConnections);

        application = applicationRepository.save(application);
        return application.getId();
    }

    @Transactional
    public String updateApplication(String id, ApplicationDTO applicationDTO, String userRole) {
        Application application = getApplicationById(id);

        modulesConnectionService.updateModulesConnection(applicationDTO.getModulesConnections(), userRole);
        application.setLastEditedDate(LocalDateTime.now());

        // when study office saves decision suggestions -> status from NEU to STUDEINBUERO
        if(!allDecisionSuggestionUnedited(application) || containsFormalRejection(application)) application.setFullStatus(STUDIENBÜRO);

        applicationRepository.save(application);
        return id;
    }

    public EnumStatusChangeAllowed updateApplicationStatusAllowed(String id) {
        Application application = getApplicationById(id);

        boolean allDecisionSuggestionEdited = allDecisionSuggestionEdited(application);
        boolean allDecisionsFinalEdited = allDecisionsFinalEdited(application);
        boolean containsFormalRejection = containsFormalRejection(application);

        if((application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU) && containsFormalRejection) return REJECT;

        if(application.getFullStatus() == ABGESCHLOSSEN) return NOT_ALLOWED;

        if(allDecisionsFinalEdited && (application.getFullStatus() == PRÜFUNGSAUSSCHUSS || application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return PASSON;

        if(allDecisionSuggestionEdited && (application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return PASSON;

        return NOT_ALLOWED;
    }

    // FUNCTION TO UPDADTE APPLICATION STATUS ON UPDATE TODO: remove unnecassary statements
    public EnumApplicationStatus updateApplicationStatus(String id) {
        Application application = getApplicationById(id);

        boolean allDecisionSuggestionEdited = allDecisionSuggestionEdited(application);
        boolean allDecisionsFinalEdited = allDecisionsFinalEdited(application);
        boolean containsFormalRejection = containsFormalRejection(application);

        if(containsFormalRejection) application.setFullStatus(FORMFEHLER);
        else if(application.getFullStatus() == FORMFEHLER) application.setFullStatus(STUDIENBÜRO);
        else if(allDecisionsFinalEdited) {
            application.setFullStatus(ABGESCHLOSSEN);
            application.setDecisionDate(LocalDateTime.now());
        } else if(allDecisionSuggestionEdited) application.setFullStatus(PRÜFUNGSAUSSCHUSS);

        applicationRepository.save(application);
        return application.getFullStatus();
    }

    // helper methos to update applicaiton status
    boolean allDecisionSuggestionUnedited(Application application) {
        boolean allDecisionSuggestionUnedited = true;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionSuggestion() != unedited)  allDecisionSuggestionUnedited = false;
        }
        return allDecisionSuggestionUnedited;
    }
    boolean allDecisionSuggestionEdited(Application application) {
        boolean allDecisionSuggestionEdited = true;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionSuggestion() == unedited)  allDecisionSuggestionEdited = false;
        }
        return allDecisionSuggestionEdited;
    }
    boolean allDecisionsFinalEdited(Application application) {
        boolean allDecisionsFinalEdited = true;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionFinal() == unedited) allDecisionsFinalEdited = false;
        }
        return allDecisionsFinalEdited;
    }
    boolean containsFormalRejection(Application application) {
        boolean containsFormalRejection = false;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getFormalRejection()) containsFormalRejection = true;
        }
        return containsFormalRejection;
    }

    // helper method for create application
    private String generateValidApplicationId() {
        String id;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) sb.append((int) (Math.random() * 10)); // generates number from 000000 to 999999
            id = sb.toString();
        }while(applicationExists(id));

        return id;
    }

    // Simple Getters for Application
    // is used internally and for login requests
    public List<Application> getAllApplciations(){
        return applicationRepository.findAll();
    }

    // is used internally and for login requests
    public Application getApplicationById(String id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            return applicationOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id: " + id + " not Found");
        }
    }
    // is used only for student request
    public Application getApplicationStudentById(String id) {
        Application application = getApplicationById(id);

        List<ModulesConnection> editModulesConnection = application.getModulesConnections();

        // return edited application, without original application TODO: delte original application when status on abgeschlossen
        if(application.getFullStatus() == ABGESCHLOSSEN) {
            List<ModulesConnection> adjModulesConnections = modulesConnectionService.removeOriginalModulesConnections(editModulesConnection);
            application.setModulesConnections(adjModulesConnections);
            return application;
        }

        // return orignal application with rejection info
        if(application.getFullStatus() == FORMFEHLER) {
            List<ModulesConnection> modulesConnectionsOriginalWithFormalRejectionData = modulesConnectionService.getOriginalModulesConnectionsWithFormalRejectionData(editModulesConnection);
            application.setModulesConnections(modulesConnectionsOriginalWithFormalRejectionData);
            return application;
        }

        // default case

        // set for student visible status
        if(application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == PRÜFUNGSAUSSCHUSS)
            application.setFullStatus(IN_BEARBEITUNG);

        // replace edited modules connection with original modules connections
        List<ModulesConnection> modulesConnectionsOriginal = modulesConnectionService.getOriginalModulesConnections(editModulesConnection);
        application.setModulesConnections(modulesConnectionsOriginal);

        return application;
    }
    public boolean applicationExists(String id) {
        return applicationRepository.existsById(id);
    }
}