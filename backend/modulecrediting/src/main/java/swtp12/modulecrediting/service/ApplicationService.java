package swtp12.modulecrediting.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.*;
import swtp12.modulecrediting.model.*;
import swtp12.modulecrediting.repository.ApplicationRepository;

import static swtp12.modulecrediting.model.ApplicationStatus.*;
import static swtp12.modulecrediting.model.ModuleConnectionDecision.*;



@Service
public class ApplicationService {
    @Autowired
    private PdfDocumentService pdfDocumentService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ModuleLeipzigService moduleLeipzigService;
    @Autowired
    private CourseLeipzigService courseLeipzigService;


    // TODO: update decisionFinal date etc...
    @Transactional
    public Long updateApplication(Long id, ApplicationUpdateDTO applicationUpdateDTO) {
        Application application = getApplicationById(id);
        List<ModulesConnection> modulesConnections = application.getModulesConnections();

        for (int i = 0; i < modulesConnections.size(); i++) {
            ModulesConnection modulesConnection = modulesConnections.get(i);
            ModuleApplication moduleApplication = modulesConnection.getModuleApplication();
            ModuleBlockUpdateDTO moduleBlockUpdateDTO = applicationUpdateDTO.getModuleBlockUpdateDTOList().get(i);

            modulesConnection.setDecisionFinal(moduleBlockUpdateDTO.getDecisionFinal());
            modulesConnection.setDecisionSuggestion(moduleBlockUpdateDTO.getDecisionSuggestion());
            modulesConnection.setCommentStudyOffice(moduleBlockUpdateDTO.getCommentStudyOffice());
            modulesConnection.setCommentDecision(moduleBlockUpdateDTO.getCommentDecision());

            moduleApplication.setName(moduleBlockUpdateDTO.getModuleName());
            moduleApplication.setUniversity(moduleBlockUpdateDTO.getUniversity());
            moduleApplication.setPoints(moduleBlockUpdateDTO.getPoints());
            moduleApplication.setPointSystem(moduleBlockUpdateDTO.getPointSystem());

            List<ModuleLeipzig> modulesLeipzig = modulesConnection.getModulesLeipzig();
            List<String> existingModuleNames = new ArrayList<>();
            for (ModuleLeipzig module : modulesLeipzig) {
                existingModuleNames.add(module.getModuleName());
            }

            // Check for new modules
            List<String> newModuleNames = new ArrayList<>();
            for (String moduleName : moduleBlockUpdateDTO.getModuleNamesLeipzig()) {
                if (!existingModuleNames.contains(moduleName)) {
                    newModuleNames.add(moduleName);
                }
            }
            List<ModuleLeipzig> modulesLeipzigNew = moduleLeipzigService.getModulesLeipzigByNames(newModuleNames);
            modulesConnection.addModulesLeipzig(modulesLeipzigNew);

            // Check for deleted modules
            List<String> deletedModuleNames = new ArrayList<>();
            for (String moduleName : existingModuleNames) {
                if (!moduleBlockUpdateDTO.getModuleNamesLeipzig().contains(moduleName)) {
                    deletedModuleNames.add(moduleName);
                }
            }
            List<ModuleLeipzig> modulesLeipzigDeleted = moduleLeipzigService.getModulesLeipzigByNames(deletedModuleNames);
            modulesConnection.removeModulesLeipzig(modulesLeipzigDeleted);

        }
        updateApplicationStatus(application);

        applicationRepository.save(application);
        return id;
    }

    public void updateApplicationStatus(Application application) {
        boolean noDecisionSuggestionCompleted = true;
        boolean allDecisionsSuggestionsCompleted = true;
        boolean allDecisionsFinalCompleted = true;


        for(ModulesConnection m : application.getModulesConnections()) {
            if(m.getDecisionSuggestion() == UNBEARBEITET) allDecisionsSuggestionsCompleted = false;
            if(m.getDecisionSuggestion() == ABGELEHNT || m.getDecisionSuggestion() == ANGENOMMEN) noDecisionSuggestionCompleted = false;
            if(m.getDecisionFinal() == UNBEARBEITET) allDecisionsFinalCompleted = false;
        }

        if(!noDecisionSuggestionCompleted) application.setFullStatus(IN_BEARBEITUNG);
        if(allDecisionsSuggestionsCompleted) application.setFullStatus(WARTEN_AUF_ENTSCHEIDUNG_DES_PRUEFUNGSAUSSCHUSSES);
        if(allDecisionsFinalCompleted) application.setFullStatus(ABGESCHLOSSEN);
    }



    public Long createApplication(ApplicationCreateDTO applicationCreateDTO) {
        ArrayList<ModulesConnection> modulesConnections = new ArrayList<>();
        for(ModuleBlockCreateDTO m : applicationCreateDTO.getModuleBlockCreateDTOList()) {
            PdfDocument pdfDocument = pdfDocumentService.createPdfDocument(m.getDescription());

            ModuleApplication moduleApplication = new ModuleApplication(m.getModuleName(), m.getPoints(), m.getPointSystem(), m.getUniversity(), m.getCommentApplicant());
            moduleApplication.addPdfDocument(pdfDocument);

            ModulesConnection modulesConnection = new ModulesConnection(ModuleConnectionDecision.UNBEARBEITET,ModuleConnectionDecision.UNBEARBEITET,"","");
            modulesConnection.addModuleApplication(moduleApplication);

            ArrayList<ModuleLeipzig> modulesLeipzig = moduleLeipzigService.getModulesLeipzigByNames(m.getModuleNamesLeipzig());
            modulesConnection.setModulesLeipzig(modulesLeipzig);

            modulesConnections.add(modulesConnection);
        }

        CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(applicationCreateDTO.getCourseLeipzig());

        Application application = new Application(OFFEN, LocalDate.now(), LocalDate.now());
        application.setCourseLeipzig(courseLeipzig);

        application.addModulesConnections(modulesConnections);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication.getId();
    }

    public List<Application> getAllApplciations(int limit, Optional<ApplicationStatus> status){
        Pageable pageeable = PageRequest.of(0, limit, Sort.by("creationDate").descending());
        if(status.isPresent()) {
            Page<Application> page = applicationRepository.findByFullStatus(status.get(), pageeable);
            return page.getContent();
        }else {
            Page<Application> page = applicationRepository.findAllBy(pageeable);
            return page.getContent();
        }
    }

    public Application getApplicationById(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            return applicationOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not Found");
        }
    }

    public boolean applicationExists(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            return true;
        }else {
            return false;
        }
    }


}
