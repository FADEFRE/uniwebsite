package swtp12.modulecrediting.service;

import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            }
            modulesConnection.setAsExamCertificate(moduleBlockUpdateDTO.getAsExamCertificate());



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


        Application application = new Application(generateValidApplicationId(), OFFEN, LocalDate.now());
        application.setCourseLeipzig(courseLeipzig);

        application.addModulesConnections(modulesConnections);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication.getId();
    }

    private String generateValidApplicationId() {
        String id;
        do {
            id = generateApplicationId();
        }while(applicationExists(id));

        return id;
    }

    private String generateApplicationId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id: " + id + " not Found");
        }
    }

    public boolean applicationExists(String id) {
        return applicationRepository.existsById(id);
    }


    public byte[] generatePdfDataDocument(String id) throws IOException, DocumentException {
        Application application = getApplicationById(id);
        List<ModulesConnection> modulesConnections = application.getModulesConnections();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        //uni-leipzig icon
        Image uniLeipzigIcon = Image.getInstance(getClass().getResource("/Universität_Leipzig_Logo.png"));
        float maxWidth = 300f;
        float maxHeight = 300f;
        uniLeipzigIcon.scaleToFit(maxWidth, maxHeight);
        float iconX = 36;
        float iconY = PageSize.A4.getHeight() - 36 - uniLeipzigIcon.getScaledHeight();
        uniLeipzigIcon.setAbsolutePosition(iconX, iconY);
        document.add(uniLeipzigIcon);

        //Title
        String titleText = "ANTRAG ZUR MODULANRECHNUNG";
        String titleText1 = "STUDIENBÜRO";
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
        Font titleFont1 = FontFactory.getFont(FontFactory.HELVETICA, 14);
        Paragraph title = new Paragraph(titleText, titleFont);
        Paragraph title1 = new Paragraph(titleText1, titleFont1);
        title.setAlignment(Element.ALIGN_LEFT);
        title1.setAlignment(Element.ALIGN_LEFT);
        title.setIndentationLeft(30f);
        title1.setIndentationLeft(30f);
        float Spacing = 150f; // Abstand zwischen Icon und Titel
        title.setSpacingBefore(Spacing);
        title1.setSpacingAfter(50);
        document.add(title);
        document.add(title1);

        // Generelle Antragsdaten
        addTable(document, "ID:", String.valueOf(id));
        addTable(document, "Status:", (application.getFullStatus() != null) ? application.getFullStatus().toString() : "");
        addTable(document, "Erstellungsdatum:", (application.getCreationDate() != null) ? application.getCreationDate().toString() : "");
        addTable(document, "Entscheidungsdatum:", (application.getDecisionDate() != null) ? application.getDecisionDate().toString() : "");

        for (ModulesConnection modulesConnection : modulesConnections) {
            ModuleApplication moduleApplication = modulesConnection.getModuleApplication();

            // Module-Anwendungsdaten
            addTable(document, "Modulname:", (moduleApplication.getName() != null) ? moduleApplication.getName() : "");
            addTable(document, "Punkte:", (moduleApplication.getPoints() != null) ? String.valueOf(moduleApplication.getPoints()) : "");
            addTable(document, "Punktesystem:", (moduleApplication.getPointSystem() != null) ? moduleApplication.getPointSystem() : "");
            addTable(document, "Universität:", (moduleApplication.getUniversity() != null) ? moduleApplication.getUniversity() : "");
            addTable(document, "Kommentar des Antragstellers:", (moduleApplication.getCommentApplicant() != null) ? moduleApplication.getCommentApplicant() : "");

            //Studibüro daten
            addTable(document, "Finale Entscheidung:", (modulesConnection.getDecisionFinal() != null) ? String.valueOf(modulesConnection.getDecisionFinal()) : "");
            addTable(document, "Kommentar zur Entscheidung:", (modulesConnection.getCommentDecision() != null) ? modulesConnection.getCommentDecision() : "");
            addTable(document, "Vorschlag zur Entscheidung:", (modulesConnection.getDecisionSuggestion() != null) ? modulesConnection.getDecisionSuggestion().toString() : "");
            addTable(document, "Kommentar des Studienbüros:", (modulesConnection.getCommentStudyOffice() != null) ? modulesConnection.getCommentStudyOffice() : "");
        }

        //Informationen Uni Leipzig
        String addressText = "Universität Leipzig\n" +
                "Dezernat Akademische Verwaltung\n" +
                "Studierendensekretariat\n" +
                "Postanschrift\n" +
                "04081 Leipzig\n\n" +
                "Besucheranschrift\n" +
                "StudierendenServiceZentrums [SSZ]\n" +
                "Goethestraße 3-5\n" +
                "04109 Leipzig\n\n" +
                "Die aktuellen Öffnungszeiten entnehmen\n" +
                "Sie bitte der Homepage:\n" +
                "www.uni-leipzig.de/ssz/";

        Font addressFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPTable addressTable = new PdfPTable(1);
        addressTable.setWidthPercentage(88);
        addressTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell addressCell = new PdfPCell(new Paragraph(addressText, addressFont));
        addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        addressCell.setBorder(Rectangle.NO_BORDER);


        addressTable.addCell(addressCell);
        addressTable.setSpacingBefore(50f);
        document.add(addressTable);

        document.close();
        return baos.toByteArray();
    }

    private void addTable(Document document, String label, String value) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(88);

        Font labelFont = FontFactory.getFont(FontFactory.TIMES, 12);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(String.valueOf(value), valueFont));


        labelCell.setBorderColorLeft(BaseColor.BLACK);
        valueCell.setBorderColorLeft(BaseColor.BLACK);

        labelCell.setBorderColorRight(BaseColor.BLACK);
        valueCell.setBorderColorRight(BaseColor.BLACK);

        labelCell.setBorderColor(BaseColor.WHITE);
        valueCell.setBorderColor(BaseColor.WHITE);

        labelCell.setBorderWidth(0);
        valueCell.setBorderWidth(0);

        table.addCell(labelCell);
        table.addCell(valueCell);

        float spacingBetweenTable = 5f;
        table.setSpacingAfter(spacingBetweenTable);

        document.add(table);
    }
}
