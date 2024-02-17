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
        context.setVariable("Entscheidungsdatum", application.getDecisionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return templateEngine.process("GeneralData", context);
    }

    public byte[] generatePdfFromHtml(String id) throws IOException {
        Application application = applicationService.getApplicationStudentById(id);
        String html = GeneralDataTemplate(id);


        OutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();

        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }
}


    /* 

    public byte[] generatePdfFromHtml(String id) throws IOException {
        StudentApplicationDTO application = getDataForPDF(id);
        String html1 = GeneralDataTemplate(id);
        String html2 = ModuleConnectionTemplate(id);
    
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
    
        ITextRenderer renderer = new ITextRenderer();
    
        renderer.setDocumentFromString(html1);
        renderer.layout();
        renderer.createPDF(outputStream1);
    
        renderer.setDocumentFromString(html2);
        renderer.layout();
        renderer.createPDF(outputStream2);
    
        byte[] mergedPdf = mergePdfs(outputStream1.toByteArray(), outputStream2.toByteArray());
    
        return mergedPdf;
    }

    private byte[] mergePdfs(byte[] pdf1, byte[] pdf2) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
        PdfDocument pdfDocument1 = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdf1)));
        PdfDocument pdfDocument2 = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdf2)));
        PdfDocument mergedPdfDocument = new PdfDocument(new PdfWriter(outputStream));
    
        pdfDocument1.copyPagesTo(1, pdfDocument1.getNumberOfPages(), mergedPdfDocument);
        pdfDocument2.copyPagesTo(1, pdfDocument2.getNumberOfPages(), mergedPdfDocument);
    
        pdfDocument1.close();
        pdfDocument2.close();
        mergedPdfDocument.close();
    
        return outputStream.toByteArray();
    }*/

