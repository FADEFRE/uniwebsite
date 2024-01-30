package swtp12.modulecrediting.service;


import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


import org.xhtmlrenderer.pdf.ITextRenderer;

import swtp12.modulecrediting.dto.StudentApplicationDTO;

import org.thymeleaf.context.Context;


@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;

    //helper to get DTO
    private StudentApplicationDTO getDataForPDF(String id) {
        return applicationService.getStudentApplicationById(id);
    }

    public String parseThymeleafTemplate(String id) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates/");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        
        
        StudentApplicationDTO application = getDataForPDF(id);

        Context context = new Context();
        context.setVariable("id", id);
        context.setVariable("Erstelldatum", application.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        context.setVariable("Status", application.getFullStatus());
        context.setVariable("Entscheidungsdatum", application.getDecisionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));


    

        return templateEngine.process("PDF", context);
    }

    public byte[] generatePdfFromHtml(String id) throws IOException {
    
        StudentApplicationDTO application = getDataForPDF(id); 
        String html = parseThymeleafTemplate(id);


        OutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();

        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }
}
