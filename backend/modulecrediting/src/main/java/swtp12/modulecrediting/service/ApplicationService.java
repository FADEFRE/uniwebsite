package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.*;
import swtp12.modulecrediting.model.*;
import swtp12.modulecrediting.repository.ApplicationRepository;
import swtp12.modulecrediting.repository.PdfDocumentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Application application = new Application("open", LocalDate.now(), LocalDate.now());
        application.setModulesConnections(modulesConnections);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication.getId();
    }

}
