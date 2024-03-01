package swtp12.modulecrediting.service;


import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
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
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;


@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;
    public static final String FONTS_JOST = "src/main/resources/static/fonts/jost";
    public static final String FONTS_ROMAN = "src/main/resources/static/fonts/TimesNewRoman";

    public String generalDataTemplate(String id) {
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
        if (status.equals("IN_BEARBEITUNG")) { status = "IN BEARBEITUNG"; }
        context.setVariable("Status", status);
        context.setVariable("Studiengang", application.getCourseLeipzig().getName());


        if (application.getDecisionDate() != null)
            context.setVariable("Entscheidungsdatum", application.getDecisionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));


        return templateEngine.process("GeneralData", context);
    }

    public String modulesConnectionsTemplate(ModulesConnection modulesConnection) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("templates/");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context modulesConnectionContext = fillModuleConnectionTemplate(modulesConnection);

        return templateEngine.process("ModulesConnection", modulesConnectionContext);
    }

    public Context fillModuleConnectionTemplate(ModulesConnection connection) {
        Context context = new Context();

        List<ExternalModule> externalModules = connection.getExternalModules();
        List<ModuleLeipzig> modulesLeipzig = connection.getModulesLeipzig();

        ExternalModule[] externalModulesArray = externalModules.toArray(new ExternalModule[externalModules.size()]);
        ModuleLeipzig[] modulesLeipzigArray = modulesLeipzig.toArray(new ModuleLeipzig[modulesLeipzig.size()]);

        StringBuilder externalModulesHeading = new StringBuilder();
        StringBuilder modulesLeipzigHeading = new StringBuilder();

        for(int i = 0; i < externalModulesArray.length; i++) {
            if(i!= 0) externalModulesHeading.append(", ");
            externalModulesHeading.append(externalModulesArray[i].getName());
        }
        for(int i = 0; i < modulesLeipzigArray.length; i++) {
            if(i!= 0) modulesLeipzigHeading.append(", ");
            modulesLeipzigHeading.append(modulesLeipzigArray[i].getName());
        }

        context.setVariable("externalModulesHeading", externalModulesHeading);
        context.setVariable("modulesLeipzigHeading", modulesLeipzigHeading);

        context.setVariable("externalModulesArray", externalModulesArray);
        context.setVariable("modulesLeipzigArray", modulesLeipzigArray);

        return context;
    }

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
        for(ModulesConnection mc : modulesConnections) {
            html.append(modulesConnectionsTemplate(mc));
        }

        HtmlConverter.convertToPdf(html.toString(), pdfDocument, properties);

        pdfDocument.close(); // Close the main PdfDocument
        return outputStream.toByteArray();
    }

}