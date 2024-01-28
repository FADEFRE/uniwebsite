package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ExternalModuleCreateDTO;
import swtp12.modulecrediting.dto.ExternalModuleUpdateDTO;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.ExternalModuleRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExternalModuleService {
    @Autowired
    PdfDocumentService pdfDocumentService;
    @Autowired
    ExternalModuleRepository externalModuleRepository;

    public Map<String, List<ExternalModule>> createExternalModules(List<ExternalModuleCreateDTO> externalModuleCreateDTOs) {
        if(externalModuleCreateDTOs == null || externalModuleCreateDTOs.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Module Applications provided in the request");
        List<ExternalModule> externalModules1 = new ArrayList<>();
        List<ExternalModule> externalModules2 = new ArrayList<>();
        Map<String, List<ExternalModule>> map = new HashMap<>();

        for(ExternalModuleCreateDTO ma : externalModuleCreateDTOs) {
            ExternalModule externalModule1 = new ExternalModule();
            ExternalModule externalModule2 = new ExternalModule();
            externalModule1.setName(ma.getName());
            externalModule1.setUniversity(ma.getUniversity());
            externalModule1.setPoints(ma.getPoints());
            externalModule1.setPointSystem(ma.getPointSystem());

            externalModule2.setName(ma.getName());
            externalModule2.setUniversity(ma.getUniversity());
            externalModule2.setPoints(ma.getPoints());
            externalModule2.setPointSystem(ma.getPointSystem());

            PdfDocument pdfDocument1 = pdfDocumentService.createPdfDocument(ma.getDescription());
            PdfDocument pdfDocument2 = pdfDocumentService.createPdfDocument(ma.getDescription());
            externalModule1.setPdfDocument(pdfDocument1);
            externalModule1.setPdfDocument(pdfDocument2);

            externalModuleRepository.save(externalModule1);
            externalModuleRepository.save(externalModule2);

            externalModule1.setMatchingId(externalModule2.getId());
            externalModule2.setMatchingId(externalModule1.getId());

            externalModules1.add(externalModule1);
            externalModules2.add(externalModule2);
        }

        map.put("one", externalModules1);
        map.put("two", externalModules2);
        return map;
    }

    public List<Long> updateExternalModules(List<ExternalModuleUpdateDTO> externalModuleUpdateDTOs, String userRole) {
        List<Long> listofIds = new ArrayList<>();
        for(ExternalModuleUpdateDTO ma : externalModuleUpdateDTOs) {
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
            if(userRole.equals("standard") && ma.getDescription() != null) {
                PdfDocument pdfDocument = pdfDocumentService.createPdfDocument(ma.getDescription());
                externalModule.setPdfDocument(pdfDocument);
            }
            listofIds.add(ma.getId());
        }
        return listofIds;
    }

    public ExternalModule getExternalModuleById(Long id) {
        Optional<ExternalModule> optionalExternalModule = externalModuleRepository.findById(id);
        if(optionalExternalModule.isPresent())
            return optionalExternalModule.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Application with id: " + id + " not Found");
    }

    public void deleteExternalModuleById(Long id) {
        ExternalModule externalModule = getExternalModuleById(id);
        ModulesConnection modulesConnection = externalModule.getModulesConnection();
        modulesConnection.removeExternalModules(List.of(externalModule));
        externalModuleRepository.deleteById(id);
    }
}
