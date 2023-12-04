package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

import jakarta.transaction.Transactional;

import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.dto.ModuleBlockCreateDTO;
import swtp12.modulecrediting.dto.ModuleBlockUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.ApplicationRepository;



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


    // TODO: Corner Case: No Module Leipzig when creating, or when updating
    // TODO: Module Accepted but only as Pruefungsschien
    @Transactional
    public String updateApplication(String id, ApplicationUpdateDTO applicationUpdateDTO) {


        Application application = getApplicationById(id);
        List<ModulesConnection> modulesConnections = application.getModulesConnections();


        if(applicationUpdateDTO.getModuleBlockUpdateDTOList() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Information is missing");
        if(applicationUpdateDTO.getModuleBlockUpdateDTOList().size() != modulesConnections.size()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules Information don't match the saved Modules Information");

        for (int i = 0; i < modulesConnections.size(); i++) {
            ModulesConnection modulesConnection = modulesConnections.get(i);
            ModuleApplication moduleApplication = modulesConnection.getModuleApplication();
            ModuleBlockUpdateDTO moduleBlockUpdateDTO = applicationUpdateDTO.getModuleBlockUpdateDTOList().get(i);

            // UPDATE BASIC MODULE APPLICATION ATTRIBUTES

            moduleApplication.setName(moduleBlockUpdateDTO.getModuleName());
            moduleApplication.setUniversity(moduleBlockUpdateDTO.getUniversity());
            moduleApplication.setPoints(moduleBlockUpdateDTO.getPoints());
            moduleApplication.setPointSystem(moduleBlockUpdateDTO.getPointSystem());

            // UPDATE PAV/STUDY_OFFICE RELATED ATTRBIUTES
            if(applicationUpdateDTO.getUserRole().equals("pav")) {
                modulesConnection.setDecisionFinal(moduleBlockUpdateDTO.getDecisionFinal());
                modulesConnection.setCommentDecision(moduleBlockUpdateDTO.getCommentDecision());
            }else if(applicationUpdateDTO.getUserRole().equals("study_office")) {
                modulesConnection.setDecisionSuggestion(moduleBlockUpdateDTO.getDecisionSuggestion());
                modulesConnection.setCommentStudyOffice(moduleBlockUpdateDTO.getCommentStudyOffice());
            }else{
                // error but UserRole is not final solutionm !
            }



            // UPDATE CONNECTED MODULES LEIPZIG
            if(moduleBlockUpdateDTO.getModuleNamesLeipzig() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Leipzig Names are missing");
            List<ModuleLeipzig> modulesLeipzig = modulesConnection.getModulesLeipzig();
            List<String> existingModuleNames = getExistingModuleNames(modulesLeipzig);
            List<String> newModuleNames = getNewModuleNames(existingModuleNames, moduleBlockUpdateDTO.getModuleNamesLeipzig());
            List<String> deletedModuleNames = getDeletedModuleNames(existingModuleNames, moduleBlockUpdateDTO.getModuleNamesLeipzig());

            List<ModuleLeipzig> modulesLeipzigNew = moduleLeipzigService.getModulesLeipzigByNames(newModuleNames);
            List<ModuleLeipzig> modulesLeipzigDeleted = moduleLeipzigService.getModulesLeipzigByNames(deletedModuleNames);

            modulesConnection.addModulesLeipzig(modulesLeipzigNew);
            modulesConnection.removeModulesLeipzig(modulesLeipzigDeleted);
        }

        // UPDATE APPLICATION STATUS (& DECISIONDATE)
        updateApplicationStatus(application);

        applicationRepository.save(application);
        return id;
    }

    public List<String> getExistingModuleNames(List<ModuleLeipzig> modulesLeipzig) {
        List<String> existingModuleNames = new ArrayList<>();
        for (ModuleLeipzig module : modulesLeipzig) {
            existingModuleNames.add(module.getModuleName());
        }
        return existingModuleNames;
    }

    public List<String> getNewModuleNames(List<String> existingModuleNames, List<String> updatedModuleNames) {
        List<String> newModuleNames = new ArrayList<>();
        for (String updatedModuleName : updatedModuleNames) {
            if (!existingModuleNames.contains(updatedModuleName)) {
                newModuleNames.add(updatedModuleName);
            }
        }
        return newModuleNames;
    }

    public List<String> getDeletedModuleNames(List<String> existingModuleNames, List<String> updatedModuleNames) {
        List<String> deletedModuleNames = new ArrayList<>();
        for (String moduleName : existingModuleNames) {
            if (!updatedModuleNames.contains(moduleName)) {
                deletedModuleNames.add(moduleName);
            }
        }
        return deletedModuleNames;
    }

    // FUNCTION TO UPDADTE APPLICATION STATUS ON UPDATE
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
        if(allDecisionsFinalCompleted) {
            application.setFullStatus(ABGESCHLOSSEN);
            application.setDecisionDate(LocalDate.now());
        }
    }



    public String createApplication(ApplicationCreateDTO applicationCreateDTO) {
        ArrayList<ModulesConnection> modulesConnections = new ArrayList<>();

        if(applicationCreateDTO.getModuleBlockCreateDTOList() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modules are required");

        for(ModuleBlockCreateDTO m : applicationCreateDTO.getModuleBlockCreateDTOList()) {
            PdfDocument pdfDocument = pdfDocumentService.createPdfDocument(m.getDescription());

            ModuleApplication moduleApplication = new ModuleApplication(m.getModuleName(), m.getPoints(), m.getPointSystem(), m.getUniversity(), m.getCommentApplicant());
            moduleApplication.addPdfDocument(pdfDocument);

            ModulesConnection modulesConnection = new ModulesConnection();
            modulesConnection.addModuleApplication(moduleApplication);

            ArrayList<ModuleLeipzig> modulesLeipzig = moduleLeipzigService.getModulesLeipzigByNames(m.getModuleNamesLeipzig());
            modulesConnection.setModulesLeipzig(modulesLeipzig);

            modulesConnections.add(modulesConnection);
        }

        CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(applicationCreateDTO.getCourseLeipzig());

        Application application = new Application(OFFEN, LocalDate.now());
        application.setCourseLeipzig(courseLeipzig);

        application.addModulesConnections(modulesConnections);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication.getId();
    }

    public List<Application> getAllApplciations(int limit, Optional<EnumApplicationStatus> status){
        Pageable pageeable = PageRequest.of(0, limit, Sort.by("creationDate").descending());
        if(status.isPresent()) {
            Page<Application> page = applicationRepository.findByFullStatus(status.get(), pageeable);
            return page.getContent();
        }else {
            Page<Application> page = applicationRepository.findAllBy(pageeable);
            return page.getContent();
        }
    }

    public Application getApplicationById(String id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            return applicationOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not Found");
        }
    }

    public boolean applicationExists(String id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        return applicationOptional.isPresent();
    }

    public byte[] generatePdfDataDocument(String id) throws IOException, DocumentException {
        Application application = getApplicationById(id);
        List<ModulesConnection> modulesConnections = application.getModulesConnections();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        //PdfWriter.getInstance(document, baos);

        for(int i = 0; i < modulesConnections.size(); i++) {
            ModulesConnection modulesConnection = modulesConnections.get(i);
            ModuleApplication moduleApplication = modulesConnection.getModuleApplication();
            PdfDocument pdfDocument = moduleApplication.getPdfDocument();
            String modulbeschreibung = new String(pdfDocument.getPdfData(), StandardCharsets.UTF_8);


            document.open();
            document.add(new Paragraph("ID Antrag: " + application.getId()));
            document.add(new Paragraph("Status: " + application.getFullStatus()));
            document.add(new Paragraph("Erstellungsdatum: " + application.getCreationDate()));
            document.add(new Paragraph("Entscheidungsdatum: " + application.getDecisionDate()));

            document.add(new Paragraph("Studiengang: " + application.getCourseLeipzig()));
            document.add(new Paragraph("Modulname: " + moduleApplication.getName()));
            document.add(new Paragraph("Punkte: " + moduleApplication.getPoints()));
            document.add(new Paragraph("Punktesystem: " + moduleApplication.getPointSystem()));
            document.add(new Paragraph("Universität: " + moduleApplication.getUniversity()));
            document.add(new Paragraph("Kommentar des Bewerbers: " + moduleApplication.getCommentApplicant()));
            document.add(new Paragraph("Modulbeschreibung: " +  modulbeschreibung));

            document.add(new Paragraph("Entscheidung: " + modulesConnection.getDecisionFinal()));
            document.add(new Paragraph("Entscheidungsvorschlag: " + modulesConnection.getDecisionSuggestion()));
            document.add(new Paragraph("Kommentar: " + modulesConnection.getCommentDecision()));
            document.add(new Paragraph("Kommentar des Studienbüros: " + modulesConnection.getCommentStudyOffice()));

        }
        document.close();
        return baos.toByteArray();
    }
}
