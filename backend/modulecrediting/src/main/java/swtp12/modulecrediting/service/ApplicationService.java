package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;
import static swtp12.modulecrediting.dto.EnumStatusChange.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.dto.EnumStatusChange;
import swtp12.modulecrediting.dto.StudentApplicationDTO;
import swtp12.modulecrediting.dto.StudentModulesConnectionDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;

// TODO: fix pdf generator

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    ModulesConnectionService modulesConnectionService;
    @Autowired
    private CourseLeipzigService courseLeipzigService;
    @Autowired
    private ModulesConnectionRepository modulesConnectionRepository;


    @Transactional
    public String updateApplication(String id, ApplicationUpdateDTO applicationDTO, String userRole) {
        Application application = getApplicationById(id);

        // check if any changes were made
        if(applicationDTO.getModulesConnections() != null)
            modulesConnectionService.updateModulesConnection(applicationDTO.getModulesConnections(), userRole);

        if(containsFormalRejection(application))
            modulesConnectionService.removeAllDecisions(application.getModulesConnections());
        application.setLastEditedDate(LocalDateTime.now());


        // corner cases, where Status is set differently
        if(!allDecisionSuggestionUnedited(application) || containsFormalRejection(application)) application.setFullStatus(STUDIENBÜRO);
        if(userRole.equals("standard")) updateApplicationStatus(id);

        applicationRepository.save(application);
        return id;
    }


    public EnumStatusChange updateApplicationStatusAllowed(String id) {
        Application application = getApplicationById(id);

        boolean allDecisionSuggestionEdited = allDecisionSuggestionEdited(application);
        boolean allDecisionsFinalEdited = allDecisionsFinalEdited(application);
        boolean containsFormalRejection = containsFormalRejection(application);

        if((application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU) && containsFormalRejection) return REJECT;
        if(application.getFullStatus() == ABGESCHLOSSEN) return NOT_ALLOWED;
        if(allDecisionsFinalEdited && (application.getFullStatus() == PRÜFUNGSAUSSCHUSS || application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return PASSON;
        if(allDecisionSuggestionEdited && (application.getFullStatus() == STUDIENBÜRO || application.getFullStatus() == NEU)) return PASSON;

        // base case
        return NOT_ALLOWED;
    }
    // FUNCTION TO UPDADTE APPLICATION STATUS ON UPDATE
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


    public StudentApplicationDTO getStudentApplicationById(String id) {
        StudentApplicationDTO studentApplicationDTO = new StudentApplicationDTO();
        Optional<Application> applicationCandidate = applicationRepository.findById(id);
        if (!applicationCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id: " + id + " not Found");

        Application application = applicationCandidate.get();
        studentApplicationDTO.setId(application.getId());
        studentApplicationDTO.setCreationDate(application.getCreationDate());
        studentApplicationDTO.setLastEditedDate(application.getLastEditedDate());
        studentApplicationDTO.setDecisionDate(application.getDecisionDate());

        CourseLeipzig courseLeipzigDTO = new CourseLeipzig();
        courseLeipzigDTO.setName(application.getCourseLeipzig().getName());
        studentApplicationDTO.setCourseLeipzig(courseLeipzigDTO);

        if (application.getFullStatus() == null) throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Application with id: " + id + " has no fullStatus");
        switch (application.getFullStatus()) {
            case FORMFEHLER:
                studentApplicationDTO.setFullStatus("FORMFEHLER");
                break;
            case ABGESCHLOSSEN:
                studentApplicationDTO.setFullStatus("ABGESCHLOSSEN");
                break;
            default:
                studentApplicationDTO.setFullStatus("IN BEARBEITUNG");
                break;
        }

        List<StudentModulesConnectionDTO> studentModulesConnectionDTOs = new ArrayList<>();
        for (ModulesConnection modulesConnection : application.getModulesConnections()) {
            StudentModulesConnectionDTO smcDTO = getStudentModulesConnectionDTO(modulesConnection.getId());
            studentModulesConnectionDTOs.add(smcDTO);
        }
        studentApplicationDTO.setModulesConnections(studentModulesConnectionDTOs);

        return studentApplicationDTO;
    }

    private StudentModulesConnectionDTO getStudentModulesConnectionDTO(Long moduleConnectionId) {
        StudentModulesConnectionDTO studentModulesConnectionDTO = new StudentModulesConnectionDTO();
        Optional<ModulesConnection> modulesConnectionCandidate = modulesConnectionRepository.findById(moduleConnectionId);
        if (!modulesConnectionCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ModulesConnection with id: " + moduleConnectionId + " not Found");

        ModulesConnection modulesConnection = modulesConnectionCandidate.get();
        studentModulesConnectionDTO.setId(modulesConnection.getId());
        
        studentModulesConnectionDTO.setCommentApplicant(modulesConnection.getCommentApplicant());

        studentModulesConnectionDTO.setExternalModules(modulesConnection.getExternalModules());
        studentModulesConnectionDTO.setModulesLeipzig(modulesConnection.getModulesLeipzig());

        if (modulesConnection.getDecisionFinal() == null) throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "ModulesConnection with id: " + moduleConnectionId + " has no decisionFinal");
        if (modulesConnection.getDecisionFinal().equals(unedited)) {
            studentModulesConnectionDTO.setDecisionFinal(unedited);
        } else {
            studentModulesConnectionDTO.setDecisionFinal(modulesConnection.getDecisionFinal());
            if (modulesConnection.getDecisionFinal() == null) throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "ModulesConnection with id: " + moduleConnectionId + " has no commentDecision");
            studentModulesConnectionDTO.setCommentDecision(modulesConnection.getCommentDecision());
        }

        studentModulesConnectionDTO.setFormalRejection(modulesConnection.getFormalRejection());
        if (modulesConnection.getFormalRejection()) studentModulesConnectionDTO.setFormalRejectionComment(modulesConnection.getFormalRejectionComment());

        return studentModulesConnectionDTO;
    }
}