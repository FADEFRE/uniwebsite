package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ModuleApplicationCreateDTO;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.model.PdfDocument;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleApplicationService {
    @Autowired
    PdfDocumentService pdfDocumentService;

    public List<ModuleApplication> createModuleApplications(List<ModuleApplicationCreateDTO> moduleApplicationsDTO) {
        if(moduleApplicationsDTO == null || moduleApplicationsDTO.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Module Applications provided in the request");
        List<ModuleApplication> moduleApplications = new ArrayList<>();

        for(ModuleApplicationCreateDTO ma : moduleApplicationsDTO) {
            ModuleApplication moduleApplication = new ModuleApplication();
            moduleApplication.setName(ma.getModuleName());
            moduleApplication.setUniversity(ma.getUniversity());
            moduleApplication.setPoints(ma.getPoints());
            moduleApplication.setPointSystem(ma.getPointSystem());

            PdfDocument pdfDocument = pdfDocumentService.createPdfDocument(ma.getDescription());
            moduleApplication.setPdfDocument(pdfDocument);

            moduleApplications.add(moduleApplication);
        }

        return moduleApplications;
    }
}
