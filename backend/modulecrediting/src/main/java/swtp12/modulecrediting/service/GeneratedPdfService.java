package swtp12.modulecrediting.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.context.Context;

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
        context.setVariable("Status", application.getFullStatus());
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