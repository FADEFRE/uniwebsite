package swtp12.modulecrediting.service;

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
import swtp12.modulecrediting.repository.projection.ApplicationProjection;
import swtp12.modulecrediting.repository.ApplicationRepository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static swtp12.modulecrediting.model.Application.ApplicationStatus.OFFEN;
import static swtp12.modulecrediting.model.ModulesConnection.ModuleConnectionDecision.UNBEARBEITET;

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
    @Autowired
    private ModulesConnectionService modulesConnectionService;


    public Long createApplication(ApplicationCreateDTO applicationCreateDTO) {
        ArrayList<ModulesConnection> modulesConnections = new ArrayList<>();
        for(ModuleBlockCreateDTO m : applicationCreateDTO.getModuleBlockCreateDTOList()) {
            PdfDocument pdfDocument = pdfDocumentService.createPdfDocument(m.getDescription());

            ModuleApplication moduleApplication = new ModuleApplication(m.getModuleName(), m.getPoints(), m.getPointSystem(), m.getUniversity(), m.getCommentApplicant());
            moduleApplication.addPdfDocument(pdfDocument);

            ModulesConnection modulesConnection = new ModulesConnection(UNBEARBEITET,UNBEARBEITET,"");
            modulesConnection.addModuleApplication(moduleApplication);

            ArrayList<ModuleLeipzig> modulesLeipzig = moduleLeipzigService.getModulesLeipzigByNames(m.getModuleNamesLeipzig());
            modulesConnection.addModulesLeipzig(modulesLeipzig);

            modulesConnections.add(modulesConnection);
        }

        CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(applicationCreateDTO.getCourseLeipzig());

        Application application = new Application(OFFEN, LocalDate.now(), LocalDate.now());
        application.setCourseLeipzig(courseLeipzig);

        application.addModulesConnections(modulesConnections);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication.getId();
    }

    public List<ApplicationProjection> getAllApplciations(int limit, Optional<Application.ApplicationStatus> status){
        Pageable pageeable = PageRequest.of(0, limit, Sort.by("creationDate").descending());
        if(status.isPresent()) {
            Page<ApplicationProjection> page = applicationRepository.findByFullStatus(status.get(), pageeable);
            return page.getContent();
        }else {
            Page<ApplicationProjection> page = applicationRepository.findAllBy(pageeable);
            return page.getContent();
        }
    }

    public ApplicationDTO getApplicationById(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            Application a = applicationOptional.get();
            CourseLeipzigWithoutModulesDTO courseLeipzig = courseLeipzigService.mapToCourseLeipzigWithoutModulesDTO(a.getCourseLeipzig());
            ApplicationDTO applicationDTO = new ApplicationDTO(a.getId(),a.getFullStatus(),a.getCreationDate(),a.getDecisionDate(),courseLeipzig, a.getModulesConnections());
            return applicationDTO;
        }else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Application not Found");
        }
    }

    public ApplicationStudentDTO getApplicationStudentById(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            Application application = applicationOptional.get();

            List<ModulesConnectionStudentDTO> modulesConnections = modulesConnectionService.mapToModulesConnectionStudentDTOList(application.getModulesConnections());
            CourseLeipzigWithoutModulesDTO courseLeipzig = courseLeipzigService.mapToCourseLeipzigWithoutModulesDTO(application.getCourseLeipzig());

            return new ApplicationStudentDTO(application.getId(),application.getFullStatus(),courseLeipzig,modulesConnections);
        }else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Application not Found");
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
