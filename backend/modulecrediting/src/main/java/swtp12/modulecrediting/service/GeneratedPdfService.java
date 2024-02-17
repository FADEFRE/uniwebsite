package swtp12.modulecrediting.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;


import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.thymeleaf.context.Context;
import swtp12.modulecrediting.model.Application;


@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;


    public String GeneralDataTemplate(String id) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates/");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);


        Application application = applicationService.getApplicationStudentById(id);

        Context context = new Context();
        context.setVariable("id", id);
        context.setVariable("Erstelldatum", application.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        context.setVariable("Status", application.getFullStatus());

        // TODO: null check can be null on not decided applicaitons -> what is displayed in this scenario?
        if(application.getDecisionDate() != null)
            context.setVariable("Entscheidungsdatum", application.getDecisionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return templateEngine.process("GeneralData", context);
    }

    public String ModulesConnectionsTemplate(String id) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates/");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();

        return templateEngine.process("ModulesConnection", context);
    }

    public byte[] generatePdfFromHtml(String id) throws DocumentException, IOException {
        String html = GeneralDataTemplate(id);
        String html2 = ModulesConnectionsTemplate(id);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();

            PdfCopy copy = new PdfCopy(document, outputStream);
            document.open();

            byte[] pdf1 = parseHtml(html);
            byte[] pdf2 = parseHtml(html2);

            addPdfToCopy(copy, pdf1);
            addPdfToCopy(copy, pdf2);

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
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream((html).getBytes()));
        document.close();
        return baos.toByteArray();
    }

    private void addPdfToCopy(PdfCopy copy, byte[] pdfBytes) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(pdfBytes);
        copy.addDocument(reader);
        reader.close();
    }
}



