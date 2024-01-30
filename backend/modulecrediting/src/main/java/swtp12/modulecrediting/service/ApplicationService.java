package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;
import static swtp12.modulecrediting.dto.EnumStatusChange.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.dto.EnumStatusChange;
import swtp12.modulecrediting.dto.ExternalModuleUpdateDTO;
import swtp12.modulecrediting.dto.ModulesConnectionUpdateDTO;
import swtp12.modulecrediting.dto.StudentApplicationDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.OriginalApplication;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.repository.ExternalModuleRepository;
import swtp12.modulecrediting.repository.ModulesConnectionRepository;
import swtp12.modulecrediting.repository.OriginalApplicationRepository;


@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private OriginalApplicationRepository originalApplicationRepository;
    @Autowired
    ModulesConnectionService modulesConnectionService;
    @Autowired
    ModulesConnectionRepository modulesConnectionRepository;
    @Autowired
    ExternalModuleRepository externalModuleRepository;
    @Autowired
    private CourseLeipzigService courseLeipzigService;


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

    @Transactional
    public String updateStudentApplication(String id, ApplicationUpdateDTO applicationDTO) {
        Application application = getApplicationById(id);
        OriginalApplication originalApplication = getOriginalApplication(id);

        if(applicationDTO.getModulesConnections() != null) {
            List<ModulesConnectionUpdateDTO> DTOs = applicationDTO.getModulesConnections();
            int size = DTOs.size();
            for (int i = 0; i < size; i++) {
                ModulesConnectionUpdateDTO dtoCon = DTOs.get(i);
                Long modConId = modulesConnectionService.getModulesConnectionById(dtoCon.getId()).getMatchingId();
                ModulesConnectionUpdateDTO modConUpDTO = duplicateModuleConnectionUpdateDTO(modConId, dtoCon);
                DTOs.add(modConUpDTO);
            }
            modulesConnectionService.updateModulesConnection(DTOs, "standard");
        }
        application.setFullStatus(NEU);
        originalApplication.setFullStatus(IN_BEARBEITUNG);
        applicationRepository.save(application);
        originalApplicationRepository.save(originalApplication);
        return id;
    }

    private ModulesConnectionUpdateDTO duplicateModuleConnectionUpdateDTO(Long id, ModulesConnectionUpdateDTO mcDto) {
        ModulesConnectionUpdateDTO modConUpDTO = new ModulesConnectionUpdateDTO();
        modConUpDTO.setId(id);
        modConUpDTO.setFormalRejection(mcDto.getFormalRejection());
        modConUpDTO.setFormalRejectionComment(mcDto.getFormalRejectionComment());
        modConUpDTO.setCommentStudyOffice(mcDto.getCommentStudyOffice());
        modConUpDTO.setDecisionSuggestion(mcDto.getDecisionSuggestion());
        modConUpDTO.setCommentDecision(mcDto.getCommentDecision());
        modConUpDTO.setDecisionFinal(mcDto.getDecisionFinal());
        modConUpDTO.setCommentApplicant(mcDto.getCommentApplicant());
        modConUpDTO.setModulesLeipzig(mcDto.getModulesLeipzig());

        List<ExternalModuleUpdateDTO> externalModules = new ArrayList<>();
        List<ExternalModuleUpdateDTO> externalModuleUpdateDTO = mcDto.getExternalModules();

        for (ExternalModuleUpdateDTO extMod : externalModuleUpdateDTO) {
            ExternalModuleUpdateDTO updateDTO = new ExternalModuleUpdateDTO();
            updateDTO.setName(extMod.getName());
            updateDTO.setDescription(extMod.getDescription());
            updateDTO.setPoints(extMod.getPoints());
            updateDTO.setPointSystem(extMod.getPointSystem());
            updateDTO.setUniversity(extMod.getUniversity());

            Optional<ExternalModule> optional = externalModuleRepository.findById(extMod.getId());
            if (!optional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExternalModule with id: " + extMod.getId() + " not found");
            Long secondId = optional.get().getMatchingId();
            updateDTO.setId(secondId);
            externalModules.add(updateDTO);
        }

        modConUpDTO.setExternalModules(externalModules);
        return modConUpDTO;
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

    @Transactional
    public String createApplication(ApplicationCreateDTO applicationDTO) {
        String id = generateValidApplicationId();
        Application application = new Application(id);
        OriginalApplication originalApplication = new OriginalApplication(id);

        CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(applicationDTO.getCourseLeipzig());
        application.setCourseLeipzig(courseLeipzig);
        originalApplication.setCourseLeipzig(courseLeipzig);


        Map<String, List<ModulesConnection>> map = modulesConnectionService.createModulesConnections(applicationDTO.getModulesConnections());

        application.addModulesConnections(map.get("one"));
        originalApplication.setModulesConnections(map.get("two"));

        application = applicationRepository.save(application);
        originalApplication = originalApplicationRepository.save(originalApplication);
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

    public List<OriginalApplication> getAllOriginalApplications() {
        return originalApplicationRepository.findAll();
    }

    public OriginalApplication getOriginalApplication(String id) {
        Optional<OriginalApplication> originalApplicationOptional = originalApplicationRepository.findById(id);
        if(!originalApplicationOptional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id: " + id + " not found");
        return originalApplicationOptional.get();
    }

    public StudentApplicationDTO getStudentApplicationById(String id) {
        StudentApplicationDTO studentApplicationDTO = new StudentApplicationDTO();

        Application application = getApplicationById(id);
        OriginalApplication originalApplication = getOriginalApplication(id);

        switch (application.getFullStatus()) {
            case FORMFEHLER:
                //return original mit ff + ff der module
                originalApplication.setFullStatus(FORMFEHLER);
                originalApplication.setLastEditedDate(application.getLastEditedDate());
                originalApplication = originalApplicationRepository.save(originalApplication);

                studentApplicationDTO = boilderPlateOriginal(originalApplication, studentApplicationDTO);

                studentApplicationDTO.setModulesConnections(getStudentModulesConnections(application, originalApplication));
                break;
            case ABGESCHLOSSEN:
                //retun application

                //originalApplicationRepository.delete(originalApplication); //TODO should original application be deleted on "ABGESCHLOSSEN"
                //or this
                originalApplication.setFullStatus(ABGESCHLOSSEN);
                originalApplication.setLastEditedDate(application.getLastEditedDate());
                originalApplication.setDecisionDate(application.getDecisionDate());
                originalApplication = originalApplicationRepository.save(originalApplication);

                studentApplicationDTO = boilderPlateApplication(application, studentApplicationDTO);

                studentApplicationDTO.setModulesConnections(application.getModulesConnections());
                break;
            default:
                //return original
                studentApplicationDTO = boilderPlateOriginal(originalApplication, studentApplicationDTO);

                studentApplicationDTO.setModulesConnections(originalApplication.getModulesConnections());
                break;
        }

        return studentApplicationDTO;
    }

    private StudentApplicationDTO boilderPlateApplication(Application application, StudentApplicationDTO studentApplicationDTO) {
        studentApplicationDTO.setId(application.getId());
        studentApplicationDTO.setFullStatus(application.getFullStatus());
        studentApplicationDTO.setCreationDate(application.getCreationDate());
        studentApplicationDTO.setLastEditedDate(application.getLastEditedDate());
        studentApplicationDTO.setDecisionDate(application.getDecisionDate()); 
        studentApplicationDTO.setCourseLeipzig(application.getCourseLeipzig());
        return studentApplicationDTO;
    }

    private StudentApplicationDTO boilderPlateOriginal(OriginalApplication originalApplication, StudentApplicationDTO studentApplicationDTO) {
        studentApplicationDTO.setId(originalApplication.getId());
        studentApplicationDTO.setFullStatus(originalApplication.getFullStatus());
        studentApplicationDTO.setCreationDate(originalApplication.getCreationDate());
        studentApplicationDTO.setLastEditedDate(originalApplication.getLastEditedDate());
        studentApplicationDTO.setDecisionDate(originalApplication.getDecisionDate()); 
        studentApplicationDTO.setCourseLeipzig(originalApplication.getOriginalCourseLeipzig());
        return studentApplicationDTO;
    }


    private List<ModulesConnection> getStudentModulesConnections(Application application, OriginalApplication originalApplication) {
        List<ModulesConnection> formfehlerModulesConnections = new ArrayList<>();
        for (ModulesConnection modulesConnection : application.getModulesConnections()) {
            Optional<ModulesConnection> optional = modulesConnectionRepository.findById(modulesConnection.getMatchingId());
            if (!optional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ModuleConnection with id: " + modulesConnection.getMatchingId() + " not found");
            ModulesConnection modCon = optional.get();

            if (modulesConnection.getFormalRejection()) { 
                modCon.setFormalRejection(true);
                modCon.setFormalRejectionComment(modulesConnection.getFormalRejectionComment());
            } 

            formfehlerModulesConnections.add(modCon);
        }
        return formfehlerModulesConnections;
    }
}