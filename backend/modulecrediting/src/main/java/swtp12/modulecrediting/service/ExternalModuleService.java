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

/**
 * This is a {@code Service} for {@link ExternalModule}
 * @author Frederik Kluge
 * @author Luca Kippe
 * @see #createExternalModules
 * @see #updateExternalModules
 * @see #getExternalModuleById
 * @see #deleteExternalModuleById
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Spring Service</a>
 */
@Service
public class ExternalModuleService {
    private ExternalModuleRepository externalModuleRepository;
    private PdfDocumentService pdfDocumentService;
    
    public ExternalModuleService(ExternalModuleRepository externalModuleRepository, @Lazy PdfDocumentService pdfDocumentService) {
        this.externalModuleRepository = externalModuleRepository;
        this.pdfDocumentService = pdfDocumentService;
    }

    /**
     * This method creates all {@link ExternalModule} definied in the given {@code List} of {@link ExternalModuleDTO}
     * @param externalModuleDTOS {@code List} of {@link ExternalModuleDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code List} of {@link ExternalModuleDTO} is {@code null} or {@code empty}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code name} in any of the {@link ExternalModuleDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code external Course} in any of the {@link ExternalModuleDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code university} in any of the {@link ExternalModuleDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code points} in any of the {@link ExternalModuleDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code point system} in any of the {@link ExternalModuleDTO} is {@code null}
     * @return {@code List} of {@link ExternalModule} created
     * @see ExternalModule
     * @see ExternalModuleDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public List<ExternalModule> createExternalModules(List<ExternalModuleDTO> externalModuleDTOS) {
        if(externalModuleDTOS == null || externalModuleDTOS.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Modules provided in the request");

        List<ExternalModule> externalModules = new ArrayList<>();

        for(ExternalModuleDTO externalModuleDTO : externalModuleDTOS) {
            ExternalModule externalModule = new ExternalModule();

            if(externalModuleDTO.getName() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module Module Name provided in the request");
            if(externalModuleDTO.getExternalCourse() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module Course provided in the request");
            if(externalModuleDTO.getUniversity() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module University provided in the request");
            if(externalModuleDTO.getPoints() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module Point System provided in the request");
            if(externalModuleDTO.getPointSystem() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No External Module Points provided in the request");

            externalModule.setName(externalModuleDTO.getName());
            externalModule.setExternalCourse(externalModuleDTO.getExternalCourse());
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
            PdfDocument pdfDocument = pdfDocumentService.getOrCreatePdfDocument(externalModuleDTO.getDescription(), pdfId);
            externalModule.setPdfDocument(pdfDocument);

            externalModules.add(externalModule);
        }

        return externalModules;
    }

    /**
     * This method updates all {@link ExternalModule} definied in the given {@code List} of {@link ExternalModuleDTO}
     * @param externalModuleUpdateDTOs {@code List} of {@link ExternalModuleDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code id} in any of the {@link ExternalModuleDTO} is {@code null}
     * @see ExternalModule
     * @see ExternalModuleDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public void updateExternalModules(List<ExternalModuleDTO> externalModuleUpdateDTOs) {
        for(ExternalModuleDTO ma : externalModuleUpdateDTOs) {
            if(ma.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Application id must not be null");
            ExternalModule externalModule = getExternalModuleById(ma.getId());
            
            if(ma.getName() != null) {
                externalModule.setName(ma.getName());
            }
            if(ma.getExternalCourse() != null) {
                externalModule.setExternalCourse(ma.getExternalCourse());
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

    /**
     * This method gets the {@link ExternalModule} with the given {@code id}
     * @param id {@code Long}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if the {@link ExternalModule} with the given {@code id} does not exists in the database
     * @return {@link ExternalModule} with the given {@code id}
     * @see ExternalModule
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public ExternalModule getExternalModuleById(Long id) {
        ExternalModule externalModule = externalModuleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module Application with id: " + id + " not Found"));
        return externalModule;
    }

    /**
     * This method deletes the {@link ExternalModule} with the given {@code id}
     * @param id {@code Long}
     */
    public void deleteExternalModuleById(Long id) {
        ExternalModule externalModule = getExternalModuleById(id);
        ModulesConnection modulesConnection = externalModule.getModulesConnection();
        modulesConnection.removeExternalModules(List.of(externalModule));
        externalModuleRepository.deleteById(id);
    }

}
