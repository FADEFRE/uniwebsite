package swtp12.modulecrediting.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.font.FontProvider;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;

/**
 * This is a {@code Service} for generating the PDF for an {@link Application}
 * @author Jonas Fischer
 * @author Luca Kippe
 * @see #generatePdfFromHtml
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Spring Service</a>
 */
@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;
    public static final String FONTS_JOST = "src/main/resources/static/fonts/jost";
    public static final String FONTS_ROMAN = "src/main/resources/static/fonts/TimesNewRoman";


    /**
     * This method creates the {@code PDF-Data} for an {@link Application} from an HTML template
     * @param id {@code String} the ID of an {@link Application}
     * @return {@code byte[]} that represents the PDF
     * @see Application
     */
    public byte[] generatePdfFromHtml(String id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        WriterProperties writerProperties = new WriterProperties();

        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addDirectory(FONTS_JOST);

        properties.setFontProvider(fontProvider);

        PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        StringBuilder html = new StringBuilder();

        // Convert the general data HTML to PDF
        html.append(generalDataTemplate(id));

        // Convert each module connection HTML to PDF and append to the main document
        Application application = applicationService.getApplicationById(id);
        List<ModulesConnection> modulesConnections = application.getModulesConnections();

        Collections.sort(modulesConnections, new Comparator<ModulesConnection>() {
            @Override
            public int compare(ModulesConnection o1, ModulesConnection o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        for(ModulesConnection mc : modulesConnections) {
            html.append(modulesConnectionsTemplate(mc));
        }

        HtmlConverter.convertToPdf(html.toString(), pdfDocument, properties);

        pdfDocument.close(); // Close the main PdfDocument
        return outputStream.toByteArray();
    }


    // ------- Private Methods -------

    /**
     * This method generates the general Data Template for the PDF of an {@link Application}
     * @param id {@code String} the ID of an {@link Application}
     * @return template as a {@code String}
     * @see Application
     */
    private String generalDataTemplate(String id) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("templates/");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);


        Application application = applicationService.getApplicationStudentById(id);

        Context context = new Context();
        context.setVariable("id", id);
        context.setVariable("Erstelldatum", application.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        String status = application.getFullStatus().toString();
        if (status.equals(EnumApplicationStatus.IN_BEARBEITUNG.toString())) status = "In Bearbeitung";
        if (status.equals(EnumApplicationStatus.NEU.toString())) status = "Neu";
        if (status.equals(EnumApplicationStatus.ABGESCHLOSSEN.toString())) status = "Abgeschlossen";
        if (status.equals(EnumApplicationStatus.FORMFEHLER.toString())) status = "Formfehler";
        context.setVariable("Status", status);

        context.setVariable("Studiengang", application.getCourseLeipzig().getName());


        if (application.getDecisionDate() == null) context.setVariable("Entscheidungsdatum", "ausstehend");
        else context.setVariable("Entscheidungsdatum", application.getDecisionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return templateEngine.process("GeneralData", context);
    }

    /**
     * This method geneates the template for a {@link ModulesConnection}
     * @param modulesConnection {@link ModulesConnection}
     * @return template as a {@code String}
     * @see ModulesConnection
     */
    private String modulesConnectionsTemplate(ModulesConnection modulesConnection) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("templates/");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context modulesConnectionContext = fillModuleConnectionTemplate(modulesConnection);

        return templateEngine.process("ModulesConnection", modulesConnectionContext);
    }

    /**
     * This method creates the {@code Context} for a {@link ModulesConnection}
     * @param connection {@link ModulesConnection}
     * @return {@code Context} of the given {@link ModulesConnection}
     * @see ModulesConnection
     * @see <a href="https://www.thymeleaf.org/apidocs/thymeleaf/2.0.2/org/thymeleaf/context/Context.html">Thymeleaf Context</a>
     */
    private Context fillModuleConnectionTemplate(ModulesConnection connection) {
        Context context = new Context();

        List<ExternalModule> externalModules = connection.getExternalModules();
        List<ModuleLeipzig> modulesLeipzig = connection.getModulesLeipzig();

        ExternalModule[] externalModulesArray = externalModules.toArray(new ExternalModule[externalModules.size()]);
        ModuleLeipzig[] modulesLeipzigArray = modulesLeipzig.toArray(new ModuleLeipzig[modulesLeipzig.size()]);

        // replace empty module code by "-"
        for (ModuleLeipzig ml :  modulesLeipzigArray) {
            if (ml.getCode().isBlank()) {
                ml.setCode("-");
            }
        }

        StringBuilder externalModulesHeading = new StringBuilder();
        StringBuilder modulesLeipzigHeading = new StringBuilder();

        for (int i = 0; i < externalModulesArray.length; i++) {
            if(i!= 0) externalModulesHeading.append(", ");
            externalModulesHeading.append(externalModulesArray[i].getName());
        }
        if (modulesLeipzig.isEmpty()) {
            modulesLeipzigHeading.append("(Module der Universität Leipzig)");
        } else {
            for (int i = 0; i < modulesLeipzigArray.length; i++) {
                if (i!= 0) modulesLeipzigHeading.append(", ");
                modulesLeipzigHeading.append(modulesLeipzigArray[i].getName());
            }
        }
        context.setVariable("externalModulesHeading", externalModulesHeading);
        context.setVariable("modulesLeipzigHeading", modulesLeipzigHeading);

        context.setVariable("externalModulesArray", externalModulesArray);
        context.setVariable("modulesLeipzigArray", modulesLeipzigArray);

        String decisionFinal = connection.getDecisionFinal().toString();
        String commentDecision = connection.getCommentDecision().toString();

        if (decisionFinal.equals(EnumModuleConnectionDecision.accepted.toString())) decisionFinal = "Angenommen";
        if (decisionFinal.equals(EnumModuleConnectionDecision.asExamCertificate.toString())) decisionFinal = "Als Übungsschein angenommen";
        if (decisionFinal.equals(EnumModuleConnectionDecision.denied.toString())) decisionFinal = "Abgelehnt";
        if (decisionFinal.equals(EnumModuleConnectionDecision.unedited.toString())) {
            decisionFinal = "ausstehend";
            commentDecision = "ausstehend";
        }

        context.setVariable("decisionFinal", decisionFinal);
        context.setVariable("commentDecision", commentDecision);

        return context;
    }

}
