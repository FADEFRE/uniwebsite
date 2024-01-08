package swtp12.modulecrediting.service;

import static com.itextpdf.text.FontFactory.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.model.ModulesConnection;

@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;

    public byte[] generatePdfDataDocument(String id) throws IOException, DocumentException {
        Application application = applicationService.getApplicationById(id);
        List<ModulesConnection> modulesConnections = application.getModulesConnections();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();
        document.newPage();

        //uni-leipzig icon
        Image uniLeipzigIcon = Image.getInstance(Objects.requireNonNull(getClass().getResource("/Universität_Leipzig_Logo.png")));
        float maxWidth = 300f;
        float maxHeight = 300f;
        uniLeipzigIcon.scaleToFit(maxWidth, maxHeight);
        float iconX = 36;
        float iconY = PageSize.A4.getHeight() - 36 - uniLeipzigIcon.getScaledHeight();
        uniLeipzigIcon.setAbsolutePosition(iconX, iconY);
        document.add(uniLeipzigIcon);

        //Title
        String titleText = "ANTRAG ZUR MODULANRECHNUNG";
        String titleText1 = "FAKULTÄT MATHEMATIK UND INFORMATIK";
        Font titleFont = getFont(HELVETICA_BOLD, 15);
        Font titleFont1 = getFont(HELVETICA, 14);
        Paragraph title = new Paragraph(titleText, titleFont);
        Paragraph title1 = new Paragraph(titleText1, titleFont1);
        title.setAlignment(Element.ALIGN_LEFT);
        title1.setAlignment(Element.ALIGN_LEFT);
        title.setIndentationLeft(30f);
        title1.setIndentationLeft(30f);
        float Spacing = 150f; // Abstand zwischen Icon und Titel
        title.setSpacingBefore(Spacing);
        title1.setSpacingAfter(30);
        document.add(title);
        document.add(title1);

        // Generelle Antragsdaten
        addTable(document, "Vorgangsnummer", String.valueOf(id));
        addTable(document, "Status", (application.getFullStatus() != null) ? application.getFullStatus().toString() : "");
        addTable(document, "Erstellungsdatum", (application.getCreationDate() != null) ? application.getCreationDate().toString() : "");
        addTable(document, "Entscheidungsdatum", (application.getDecisionDate() != null) ? application.getDecisionDate().toString() : "");

        String addressText = "Universität Leipzig\n" +
                "Studienbüro Fakultät für Mathematik und Informatik\n" +
                "Neues Augusteum\n" +
                "04109 Leipzig\n\n" +
                "Leitung: Marco Neumann\n\n" +
                "Telefon: +49 341 97-32165\n" +
                "Telefax: +49 341 97-32193\n" +
                "Email-Adresse: studienbuero@math.uni-leipzig.de\n\n" +
                "Die aktuellen Sprechzeiten entnehmen\n" +
                "Sie bitte der Homepage:\n" +
                "www.mathcs.uni-leipzig.de/studium/studienbuero\n";

        Font addressFont = getFont(HELVETICA, 10);
        PdfPTable addressTable = new PdfPTable(1);
        addressTable.setWidthPercentage(88);
        addressTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell addressCell = new PdfPCell(new Paragraph(addressText, addressFont));
        addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        addressCell.setBorder(Rectangle.NO_BORDER);


        addressTable.addCell(addressCell);
        addressTable.setSpacingBefore(50f);
        document.add(addressTable);


        for (int i = 0; i < modulesConnections.size(); i += 2) {
            document.newPage();

            ModulesConnection modulesConnection1 = modulesConnections.get(i);


            List<ModuleApplication> moduleApplications1 = modulesConnection1.getModuleApplications();

            for (ModuleApplication moduleApplication1 : moduleApplications1) {

                //uni-leipzig icon
                document.add(uniLeipzigIcon);

                //Title
                String titleText2 = "MODUL";
                Font titleFont2 = getFont(HELVETICA_BOLD, 15);
                Font titleFont3 = getFont(HELVETICA, 14);
                Paragraph title2 = new Paragraph(titleText2, titleFont2);
                Paragraph title3 = new Paragraph(moduleApplication1.getName().toUpperCase(), titleFont3);
                title2.setAlignment(Element.ALIGN_LEFT);
                title3.setAlignment(Element.ALIGN_LEFT);
                title2.setIndentationLeft(30f);
                title3.setIndentationLeft(30f);
                float Spacing1 = 150f; // Abstand zwischen Icon und Titel
                title2.setSpacingBefore(Spacing1);
                title3.setSpacingAfter(30);
                document.add(title2);
                document.add(title3);

                // Module-Anwendungsdaten
                addTable(document, "Punkte", (moduleApplication1.getPoints() != null) ? String.valueOf(moduleApplication1.getPoints()) : "");
                addTable(document, "Punktesystem", (moduleApplication1.getPointSystem() != null) ? moduleApplication1.getPointSystem() : "");
                addTable(document, "Universität", (moduleApplication1.getUniversity() != null) ? moduleApplication1.getUniversity() : "");

                //Studibüro daten
                addTable(document, "Finale Entscheidung", (modulesConnection1.getDecisionFinal() != null) ? String.valueOf(modulesConnection1.getDecisionFinal()) : "");
                addTable(document, "Kommentar zur Entscheidung", (modulesConnection1.getCommentDecision() != null) ? modulesConnection1.getCommentDecision() : "");
            }
            if (i + 1 < modulesConnections.size()) {
                ModulesConnection modulesConnection2 = modulesConnections.get(i + 1);
                List<ModuleApplication> moduleApplications2 = modulesConnection2.getModuleApplications();

                for (ModuleApplication moduleApplication2 : moduleApplications2) {
                    String titleText4 = "MODUL";
                    Font titleFont4 = getFont(HELVETICA_BOLD, 15);
                    Font titleFont5 = getFont(HELVETICA, 14);
                    Paragraph title4 = new Paragraph(titleText4, titleFont4);
                    Paragraph title5 = new Paragraph(moduleApplication2.getName().toUpperCase(), titleFont5);
                    title4.setAlignment(Element.ALIGN_LEFT);
                    title5.setAlignment(Element.ALIGN_LEFT);
                    title4.setIndentationLeft(30f);
                    title5.setIndentationLeft(30f);
                    float Spacing2 = 100f; // Abstand zwischen Icon und Titel
                    title4.setSpacingBefore(Spacing2);
                    title5.setSpacingAfter(50);
                    document.add(title4);
                    document.add(title5);

                    // Module-Anwendungsdaten
                    addTable(document, "Punkte", (moduleApplication2.getPoints() != null) ? String.valueOf(moduleApplication2.getPoints()) : "");
                    addTable(document, "Punktesystem", (moduleApplication2.getPointSystem() != null) ? moduleApplication2.getPointSystem() : "");
                    addTable(document, "Universität", (moduleApplication2.getUniversity() != null) ? moduleApplication2.getUniversity() : "");

                    //Studibüro daten
                    addTable(document, "Finale Entscheidung", (modulesConnection2.getDecisionFinal() != null) ? String.valueOf(modulesConnection2.getDecisionFinal()) : "");
                    addTable(document, "Kommentar zur Entscheidung", (modulesConnection2.getCommentDecision() != null) ? modulesConnection2.getCommentDecision() : "");
                }
            }
        }
        document.close();
        return baos.toByteArray();
    }

    private void addTable(Document document, String label, String value) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(88);

        Font labelFont = getFont(HELVETICA, 12);
        Font valueFont = getFont(FontFactory.HELVETICA_BOLD, 12);

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
