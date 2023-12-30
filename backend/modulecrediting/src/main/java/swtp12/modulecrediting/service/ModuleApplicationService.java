package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ModuleApplicationCreateDTO;
import swtp12.modulecrediting.dto.ModuleApplicationUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.ModuleApplicationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleApplicationService {
    @Autowired
    PdfDocumentService pdfDocumentService;
    @Autowired
    ModuleApplicationRepository moduleApplicationRepository;

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

    public void updateModuleApplications(List<ModuleApplicationUpdateDTO> moduleApplicationsDTO) {
        for(ModuleApplicationUpdateDTO ma : moduleApplicationsDTO) {
            if(ma.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Application id must not be null");
            ModuleApplication moduleApplication = getModuleApplicationById(ma.getId());

            if(ma.getDelete() != null && ma.getDelete()) {
                deleteModuleApplicationById(ma.getId());
                return;
            }

            if(ma.getModuleName() != null)
                moduleApplication.setName(ma.getModuleName());

            if(ma.getUniversity() != null)
                moduleApplication.setUniversity(ma.getUniversity());

            if(ma.getPoints() != null)
                moduleApplication.setPoints(ma.getPoints());

            if(ma.getPointSystem() != null)
                moduleApplication.setPointSystem(ma.getPointSystem());

        }

    }

    public ModuleApplication getModuleApplicationById(Long id) {
        Optional<ModuleApplication> optionalModuleApplication = moduleApplicationRepository.findById(id);
        if(optionalModuleApplication.isPresent())
            return optionalModuleApplication.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Application with id: " + id + " not Found");
    }

    public void deleteModuleApplicationById(Long id) {
        ModuleApplication moduleApplication = getModuleApplicationById(id);
        moduleApplication.getModulesConnection().removeModuleApplications(List.of(moduleApplication));
        moduleApplicationRepository.deleteById(id);
    }
}
