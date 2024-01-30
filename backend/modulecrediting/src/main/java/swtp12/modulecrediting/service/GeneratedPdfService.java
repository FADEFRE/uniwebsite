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

import org.thymeleaf.context.Context;
import swtp12.modulecrediting.model.Application;


@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;

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
        Application application = applicationService.getApplicationStudentById(id);
        String html = parseThymeleafTemplate();


        OutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();

        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }
}
