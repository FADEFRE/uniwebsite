package swtp12.modulecrediting.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.*;
import swtp12.modulecrediting.model.*;
import swtp12.modulecrediting.repository.ApplicationProjection;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.repository.PdfDocumentRepository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    PdfDocumentService pdfDocumentService;
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    PdfDocumentRepository pdfDocumentRepository;


    public Long createApplication(ApplicationCreateDTO applicationCreateDTO) {
        ArrayList<ModulesConnection> modulesConnections = new ArrayList<>();
        for(ModuleBlockCreateDTO m : applicationCreateDTO.getModuleBlockCreateDTOList()) {
            PdfDocument pdfDocument = pdfDocumentService.createPdfDocument(m.getDescription());

            ModuleApplication moduleApplication = new ModuleApplication(m.getModuleName(), m.getPoints(), m.getPointSystem(), m.getUniversity(), m.getCommentApplicant());
            moduleApplication.setPdfDocument(pdfDocument);

            ModulesConnection modulesConnection = new ModulesConnection("open", "");
            modulesConnection.setModuleApplication(moduleApplication);

            // find module leipzig...

            modulesConnections.add(modulesConnection);
        }

        Application application = new Application("open", LocalDate.now(), LocalDate.now(), applicationCreateDTO.getCourseLeipzig());
        application.setModulesConnections(modulesConnections);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication.getId();
    }

    public List<ApplicationProjection> getAllApplciations(int limit){
        Pageable pageeable = PageRequest.of(0, limit);
        Page<ApplicationProjection> page = applicationRepository.findAllBy(pageeable);
        return page.getContent();
    }

    public Application getApplicationById(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if(applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            return application;
        }else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Application not Found");
        }
    }
}
