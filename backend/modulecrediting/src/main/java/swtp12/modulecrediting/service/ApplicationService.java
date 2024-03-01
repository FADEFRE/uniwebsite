package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.dto.EnumStatusChangeAllowed.*;
import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.unedited;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import swtp12.modulecrediting.dto.ApplicationDTO;
import swtp12.modulecrediting.dto.EnumStatusChangeAllowed;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.util.LogUtil;


@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    private ModulesConnectionService modulesConnectionService;
    private CourseLeipzigService courseLeipzigService;

    public ApplicationService(@Lazy ModulesConnectionService modulesConnectionService, @Lazy CourseLeipzigService courseLeipzigService) {
        this.modulesConnectionService = modulesConnectionService;
        this.courseLeipzigService = courseLeipzigService;
    }

    public String createApplication(ApplicationDTO applicationDTO) {
        String id = generateValidApplicationId();
        Application application = new Application(id);

        CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(applicationDTO.getCourseLeipzig());
        application.setCourseLeipzig(courseLeipzig);

        List<ModulesConnection> modulesConnections = modulesConnectionService.createModulesConnectionsWithDuplicate(applicationDTO.getModulesConnections());
        application.setModulesConnections(modulesConnections);

        application = applicationRepository.save(application);
        LogUtil.printApplicationLog(LogUtil.ApplicationType.CREATED, application.getId());
        return application.getId();
    }

    @Transactional
    public String updateApplicationAfterFormalRejection(String id, ApplicationDTO applicationDTO) {
        Application application = getApplicationById(id);

        if(application.getFullStatus() != FORMFEHLER)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only FORMFEHLER applications can be updated by student");

        application.setFullStatus(STUDIENBÜRO);
        application.setLastEditedDate(LocalDateTime.now());

        // create new modules connections (before deleting otherwise old pdf documetns are already deleted)
        List<ModulesConnection> modulesConnections = modulesConnectionService.createModulesConnectionsWithDuplicate(applicationDTO.getModulesConnections());

        // delete old modules connections/
        application.removeAllModulesConnections();

        application.addModulesConnections(modulesConnections);
        application = applicationRepository.save(application);
        LogUtil.printApplicationLog(LogUtil.ApplicationType.RESUBMIT, application.getId());
        return application.getId();
    }

    @Transactional
    public String updateApplication(String id, ApplicationDTO applicationDTO, String userRole) {
        Application application = getApplicationById(id);

        modulesConnectionService.updateModulesConnection(applicationDTO.getModulesConnections(), userRole);
        application.setLastEditedDate(LocalDateTime.now());

        // updating status when saving NEU application
        if(application.getFullStatus() == NEU && userRole.equals("study-office")) application.setFullStatus(STUDIENBÜRO);
        if(application.getFullStatus() == NEU && userRole.equals("chairman")) application.setFullStatus(PRÜFUNGSAUSSCHUSS);
        // updating status after NEU EINREICHEN to study office
        if(containsFormalRejection(application) && application.getFullStatus() == FORMFEHLER) application.setFullStatus(STUDIENBÜRO);

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

        if(allDecisionsFinalEdited && (application.getFullStatus() == PRÜFUNGSAUSSCHUSS || application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return PASS_ON;

        if(allDecisionSuggestionEdited && (application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return PASS_ON;

        return NOT_ALLOWED;
    }

    // FUNCTION TO UPDADTE APPLICATION STATUS ON UPDATE
    public EnumApplicationStatus updateApplicationStatus(String id) {
        Application application = getApplicationById(id);

        boolean allDecisionSuggestionEdited = allDecisionSuggestionEdited(application);
        boolean allDecisionsFinalEdited = allDecisionsFinalEdited(application);
        boolean containsFormalRejection = containsFormalRejection(application);

        if(containsFormalRejection) {
            application.setFullStatus(FORMFEHLER);

            applicationRepository.save(application);
            LogUtil.printApplicationLog(LogUtil.ApplicationType.FORMAL_REJECTION, application.getId());
            return application.getFullStatus();
        }
        else if(allDecisionsFinalEdited) {
            application.setFullStatus(ABGESCHLOSSEN);
            application.setDecisionDate(LocalDateTime.now());
            modulesConnectionService.deleteOriginalModulesConnections(application.getModulesConnections());

            applicationRepository.save(application);
            LogUtil.printApplicationLog(LogUtil.ApplicationType.FINISHED, application.getId());
            return application.getFullStatus();
        }
        else if(allDecisionSuggestionEdited) {
            application.setFullStatus(PRÜFUNGSAUSSCHUSS);

            applicationRepository.save(application);
            LogUtil.printApplicationLog(LogUtil.ApplicationType.PASSED_ON, application.getId());
            return application.getFullStatus();
        }

        applicationRepository.save(application);
        return application.getFullStatus();
    }

    private boolean allDecisionSuggestionEdited(Application application) {
        boolean allDecisionSuggestionEdited = true;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionSuggestion() == unedited)  allDecisionSuggestionEdited = false;
        }
        return allDecisionSuggestionEdited;
    }
    private boolean allDecisionsFinalEdited(Application application) {
        boolean allDecisionsFinalEdited = true;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionFinal() == unedited) allDecisionsFinalEdited = false;
        }
        return allDecisionsFinalEdited;
    }
    private boolean containsFormalRejection(Application application) {
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
    // is used internally
    public List<Application> getAllApplciations(){
        return applicationRepository.findAll();
    }

    // is used internally
    public Application getApplicationById(String id) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id: " + id + " not Found"));
        return application;
    }
    // is used only for student request
    public Application getApplicationStudentById(String id) {
        Application application = getApplicationById(id);

        List<ModulesConnection> editModulesConnection = application.getModulesConnections();

        // return edited application, (original modules connections are deleted)
        if(application.getFullStatus() == ABGESCHLOSSEN) {
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