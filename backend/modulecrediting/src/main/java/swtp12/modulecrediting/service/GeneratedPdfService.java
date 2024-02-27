package swtp12.modulecrediting.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.context.Context;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;


@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;


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


        if(application.getDecisionDate() != null)
            context.setVariable("Entscheidungsdatum", application.getDecisionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return templateEngine.process("GeneralData", context);
    }

    public String modulesConnectionsTemplate(String id, Context context) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("templates/");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("ModulesConnection", context);
    }

    public Context fillModuleConnectionTemplate(String id, ModulesConnection connection) {
        Context context = new Context();

        List<ExternalModule> externalModules = connection.getExternalModules();
        List<ModuleLeipzig> modulesLeipzig = connection.getModulesLeipzig();

        ExternalModule[] externalModulesArray = externalModules.toArray(new ExternalModule[externalModules.size()]);
        ModuleLeipzig[] modulesLeipzigArray = modulesLeipzig.toArray(new ModuleLeipzig[modulesLeipzig.size()]);

        context.setVariable("externalModulesArray", externalModulesArray);
        context.setVariable("modulesLeipzigArray", modulesLeipzigArray);

        return context;
    }

    public List<ModulesConnection> getModulesConnections(String id){
        Application application = applicationService.getApplicationById(id);
        List<ModulesConnection> modulesConnections = application.getModulesConnections();

        return modulesConnections;
    }

    public byte[] generatePdfFromHtml(String id) throws DocumentException, IOException {
        String generalDataHtml = generalDataTemplate(id);
        List<ModulesConnection> modulesConnections = getModulesConnections(id);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();

            PdfCopy copy = new PdfCopy(document, outputStream);
            document.open();

            byte[] generalDataPdf = parseHtml(generalDataHtml);
            addPdfToCopy(copy, generalDataPdf);

            for (ModulesConnection connection : modulesConnections) {
                Context context = fillModuleConnectionTemplate(id, connection);
                String connectionHtml = modulesConnectionsTemplate(id, context);
                byte[] connectionPdf = parseHtml(connectionHtml);
                addPdfToCopy(copy, connectionPdf);
            }

            document.close();
        } finally {
            outputStream.close();
        }
        return outputStream.toByteArray();
    }

    private byte[] parseHtml(String html) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

         //Create a FontProvider and register your custom font
        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontProvider.register("/static/fonts/Crimson-Roman.ttf", "Roman");
        fontProvider.register("/static/fonts/Jost-Medium.ttf", "Jost-Medium");
        fontProvider.register("/static/fonts/Jost-SemiBold.ttf", "Jost-SemiBold");
        fontProvider.register("/static/fonts/Jost-Bold.ttf", "Jost-Bold");


         //Parse the HTML into the document using the custom FontProvider
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()), null, null, fontProvider);

        document.close();
        return baos.toByteArray();
    }


    private void addPdfToCopy(PdfCopy copy, byte[] pdfBytes) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(pdfBytes);
        copy.addDocument(reader);
        reader.close();
    }
}



