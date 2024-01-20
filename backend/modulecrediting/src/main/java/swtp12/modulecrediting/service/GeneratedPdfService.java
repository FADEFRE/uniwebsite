package swtp12.modulecrediting.service;


import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lowagie.text.DocumentException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.layout.SharedContext;


@Service
public class GeneratedPdfService {
    @Autowired
    private ApplicationService applicationService;


    public byte[] generatePdfFromHtml() throws DocumentException, IOException {
        try (OutputStream os = new ByteArrayOutputStream()) {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("PDF.html");


            if (inputStream == null) {
                throw new FileNotFoundException("PDF.html konnte nicht gefunden werden");
            }
            String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Document doc = Jsoup.parse(htmlContent);
            doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

            ITextRenderer renderer = new ITextRenderer();
            SharedContext context = renderer.getSharedContext();
            context.setPrint(true);
            context.setInteractive(false);

            String baseUrl = classLoader.getResource("PDF.html").toString();
            renderer.setDocumentFromString(doc.html(), baseUrl);
            renderer.layout();
            renderer.createPDF(os);

            return ((ByteArrayOutputStream) os).toByteArray();
        }
    }
}
