package swtp12.modulecrediting.service;


import java.io.IOException;
import java.io.OutputStream;

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

    //helper to get DTO
    private Application getDataForPDF(String id) {
        return applicationService.getApplicationStudentById(id);
    }

    public String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates/");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("to", "Baeldung");

        return templateEngine.process("PDF", context);
    }

    public byte[] generatePdfFromHtml(String id) throws IOException {
        /*
            Hey Jonas, wir haben Dinge umgebaut, deswegen habe ich dir hier das DTO schon rein geschrieben
            und die function mit der ID erweitert. Alle Daten des Antrags die du im Pdf anzeigen willst
            oder kannst, sind in diesem DTO, du kannst dir dann einfach die Felder über die Getter holen :)
        */
        Application application = getDataForPDF(id);
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
