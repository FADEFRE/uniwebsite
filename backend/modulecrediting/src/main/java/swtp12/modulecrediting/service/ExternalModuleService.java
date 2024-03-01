package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.ExternalModuleDTO;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.ExternalModuleRepository;

@Service
public class ExternalModuleService {
    ExternalModuleRepository externalModuleRepository;
    PdfDocumentService pdfDocumentService;
    
    public ExternalModuleService(ExternalModuleRepository externalModuleRepository, @Lazy PdfDocumentService pdfDocumentService) {
        this.externalModuleRepository = externalModuleRepository;
        this.pdfDocumentService = pdfDocumentService;
    }


    public List<ExternalModule> createExternalModules(List<ExternalModuleDTO> externalModuleDTOS) {
        if(externalModuleDTOS == null || externalModuleDTOS.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Modules provided in the request");

        List<ExternalModule> externalModules = new ArrayList<>();

        for(ExternalModuleDTO externalModuleDTO : externalModuleDTOS) {
            ExternalModule externalModule = new ExternalModule();

            if(externalModuleDTO.getName() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module Module Name provided in the request");
            if(externalModuleDTO.getUniversity() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module University provided in the request");
            if(externalModuleDTO.getPoints() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module Point System provided in the request");
            if(externalModuleDTO.getPointSystem() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module Points provided in the request");

            externalModule.setName(externalModuleDTO.getName());
            externalModule.setUniversity(externalModuleDTO.getUniversity());
            externalModule.setPoints(externalModuleDTO.getPoints());
            externalModule.setPointSystem(externalModuleDTO.getPointSystem());

            Long pdfId = null;
            if (externalModuleDTO.getId() != null) {
                pdfId = externalModuleRepository
                    .findById(externalModuleDTO.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "External Module with given id not found"))
                    .getPdfDocument()
                    .getId();
            }
            PdfDocument pdfDocument = pdfDocumentService.createOrGetPdfDocument(externalModuleDTO.getDescription(), pdfId);
            externalModule.setPdfDocument(pdfDocument);

            externalModules.add(externalModule);
        }

        return externalModules;
    }


    public void updateExternalModules(List<ExternalModuleDTO> externalModuleUpdateDTOs) {
        for(ExternalModuleDTO ma : externalModuleUpdateDTOs) {
            if(ma.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Application id must not be null");
            ExternalModule externalModule = getExternalModuleById(ma.getId());
            
            if(ma.getName() != null) {
                externalModule.setName(ma.getName());
            }
            if(ma.getUniversity() != null) {
                externalModule.setUniversity(ma.getUniversity());
            }
            if(ma.getPoints() != null) {
                externalModule.setPoints(ma.getPoints());
            }
            if(ma.getPointSystem() != null) {
                externalModule.setPointSystem(ma.getPointSystem());
            }
        }
    }



    public ExternalModule getExternalModuleById(Long id) {
        ExternalModule externalModule = externalModuleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Application with id: " + id + " not Found"));
        return externalModule;
    }

    public void deleteExternalModuleById(Long id) {
        ExternalModule externalModule = getExternalModuleById(id);
        ModulesConnection modulesConnection = externalModule.getModulesConnection();
        modulesConnection.removeExternalModules(List.of(externalModule));
        externalModuleRepository.deleteById(id);
    }
}
