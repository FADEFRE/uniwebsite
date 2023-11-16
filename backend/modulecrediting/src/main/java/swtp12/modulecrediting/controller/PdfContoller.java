package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.Pdf;
import swtp12.modulecrediting.repository.PdfRepository;

@RestController
@RequestMapping("/pdf")
public class PdfContoller {

    private final PdfRepository pdfRepository;

    public PdfContoller(PdfRepository pdfRepository) {
        this.pdfRepository = pdfRepository;
    }

    @GetMapping
    List<Pdf> all() {
        return pdfRepository.findAll();
    }

}
