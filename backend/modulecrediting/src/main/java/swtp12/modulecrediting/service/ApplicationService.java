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

/**
 * This is a {@code Service} for {@link Application}
 * @author Frederik Kluge
 * @author Luca Kippe
 * @see #getAllApplciations
 * @see #getApplicationById
 * @see #getApplicationStudentById
 * @see #createApplication
 * @see #updateApplicationAfterFormalRejection
 * @see #updateApplication
 * @see #updateApplicationStatusAllowed
 * @see #updateApplicationStatus
 * @see #applicationExists
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Spring Service</a>
 */
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


    /**
     * This method returns all {@link Application} in the database. This method is used for the "internal" requests
     * @return {@code List} of all {@link Application}
     * @see Application
     */
    public List<Application> getAllApplciations(){
        return applicationRepository.findAll();
    }

    /**
     * This method returns the {@link Application} with the given {@code id}. This method is used for the "internal" requests
     * @param id {@code String}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if a {@link Application} with the given {@code id} could not be found
     * @return {@link Application} with the given {@code id}
     * @see Application
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public Application getApplicationById(String id) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id: " + id + " not Found"));
        return application;
    }

    /**
     * This method returns the "student" version of the {@link Application} with the given {@code id}. This method is used for the "student" requests
     * @param id {@code String}
     * @return the "student" version of the {@link Application} with the given {@code id}
     * @see Application
     */
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
    
    /**
     * This method creates an {@link Application} definied by the given {@link ApplicationDTO}
     * @param applicationDTO {@link ApplicationDTO}
     * @return {@code String} that is the {@code id} of the created {@link Application}
     * @see Application
     * @see ApplicationDTO
     */
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

    /**
     * This method updates the {@link Application} with the given {@code id} definied by the given {@link ApplicationDTO}
     * @param id {@code String}
     * @param applicationDTO {@link ApplicationDTO}
     * @throws ResponseStatusException with {@code HttpStatus.UNAUTHORIZED: 401} if the to be updated {@link Application} does not have a "FORMFEHLER"
     * @return {@code String} that is the {@code id} of the updated {@link Application}
     * @see Application
     * @see ApplicationDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
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

    /**
     * This method updates the {@link Application} with the given {@code id} definied by the given {@link ApplicationDTO} depending on the given {@code userRole}
     * @param id {@code String}
     * @param applicationDTO {@link ApplicationDTO}
     * @param userRole {@code String}
     * @return {@code String} that is the {@code id} of the updated {@link Application}
     * @see Application
     * @see ApplicationDTO
     */
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

    /**
     * This method returns an {@link EnumStatusChangeAllowed} with the value depending on the {@code Status} of the {@link Application} with the given {@code id}
     * @param id {@code String}
     * @return {@link EnumStatusChangeAllowed} with the value depending on the {@code Status} of the {@link Application}
     * @see Application
     * @see EnumStatusChangeAllowed
     */
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

    /**
     * This method updates the {@link EnumApplicationStatus} of the {@link Application} with the given {@code id}
     * @param id {@code String}
     * @return {@link EnumApplicationStatus} of the {@link Application}
     * @see Application
     * @see EnumApplicationStatus
     */
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

    /**
     * This method checks if an {@link Application} with the given {@code id} exists
     * @param id {@code String}
     * @return {@code True} if an {@link Application} with the given {@code id} exists
     * @see Application
     */
    public boolean applicationExists(String id) {
        return applicationRepository.existsById(id);
    }

    
    // ------- Package Methods ------- Theses methods should be updated to public or private

    /**
     * This method checks if all {@code DecisionSuggestion} have been edited for a given {@link Application}
     * @param application {@link Application}
     * @return {@code True} if all {@code DecisionSuggestion} have been edited for the given {@link Application}
     * @see Application
     */
    boolean allDecisionSuggestionEdited(Application application) {
        boolean allDecisionSuggestionEdited = true;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionSuggestion() == unedited)  allDecisionSuggestionEdited = false;
        }
        return allDecisionSuggestionEdited;
    }

    /**
     * This method checks if all {@code DecisionsFinal} have been edited for a given {@link Application}
     * @param application {@link Application}
     * @return {@code True} if all {@code DecisionsFinal} have been edited for the given {@link Application}
     * @see Application
     */
    boolean allDecisionsFinalEdited(Application application) {
        boolean allDecisionsFinalEdited = true;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionFinal() == unedited) allDecisionsFinalEdited = false;
        }
        return allDecisionsFinalEdited;
    }
    
    /**
     * This method checks if a given {@link Application} contains any {@code FormalRejection}
     * @param application {@link Application}
     * @return {@code True} if any {@code FormalRejection} have been found for the given {@link Application}
     * @see Application
     */
    boolean containsFormalRejection(Application application) {
        boolean containsFormalRejection = false;
        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getFormalRejection()) containsFormalRejection = true;
        }
        return containsFormalRejection;
    }

    /**
     * This method generates a valid {@code id} for an {@link Application}
     * @return valid {@code id} for an {@link Application}
     */
    String generateValidApplicationId() {
        String id;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) sb.append((int) (Math.random() * 10)); // generates number from 000000 to 999999
            id = sb.toString();
        }while(applicationExists(id));

        return id;
    }

}
