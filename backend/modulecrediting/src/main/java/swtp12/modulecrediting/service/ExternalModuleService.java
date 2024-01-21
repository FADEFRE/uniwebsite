package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swtp12.modulecrediting.dto.ExternalModuleCreateDTO;
import swtp12.modulecrediting.dto.ExternalModuleUpdateDTO;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.PdfDocument;
import swtp12.modulecrediting.repository.ExternalModuleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalModuleService {
    @Autowired
    PdfDocumentService pdfDocumentService;
    @Autowired
    ExternalModuleRepository externalModuleRepository;

    public List<ExternalModule> createExternalModules(List<ExternalModuleCreateDTO> externalModuleCreateDTOs) {
        if(externalModuleCreateDTOs == null || externalModuleCreateDTOs.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Module Applications provided in the request");
        List<ExternalModule> externalModules = new ArrayList<>();

        for(ExternalModuleCreateDTO ma : externalModuleCreateDTOs) {
            ExternalModule externalModule = new ExternalModule();
            externalModule.setName(ma.getName());
            externalModule.setUniversity(ma.getUniversity());
            externalModule.setPoints(ma.getPoints());
            externalModule.setPointSystem(ma.getPointSystem());

            PdfDocument pdfDocument = pdfDocumentService.createPdfDocument(ma.getDescription());
            externalModule.setPdfDocument(pdfDocument);

            externalModules.add(externalModule);
        }

        return externalModules;
    }

    public void updateExternalModules(List<ExternalModuleUpdateDTO> externalModuleUpdateDTOs) {
        for(ExternalModuleUpdateDTO ma : externalModuleUpdateDTOs) {
            if(ma.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Application id must not be null");
            ExternalModule externalModule = getExternalModuleById(ma.getId());


            if(ma.getName() != null)
                externalModule.setName(ma.getName());

            if(ma.getUniversity() != null)
                externalModule.setUniversity(ma.getUniversity());

            if(ma.getPoints() != null)
                externalModule.setPoints(ma.getPoints());

            if(ma.getPointSystem() != null)
                externalModule.setPointSystem(ma.getPointSystem());

        }

    }

    public ExternalModule getExternalModuleById(Long id) {
        Optional<ExternalModule> optionalExternalModule = externalModuleRepository.findById(id);
        if(optionalExternalModule.isPresent())
            return optionalExternalModule.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Application with id: " + id + " not Found");
    }

    public void deleteExternalModuleById(Long id) {
        ExternalModule externalModule = getExternalModuleById(id);
        externalModule.getModulesConnection().removeExternalModules(List.of(externalModule));
        externalModuleRepository.deleteById(id);
    }
}
