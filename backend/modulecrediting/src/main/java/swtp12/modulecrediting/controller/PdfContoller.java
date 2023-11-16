package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.Pdf;
import swtp12.modulecrediting.repository.PdfRepository;

@RestController
@RequestMapping("/pdf")
public class PdfContoller {

    @Autowired
    private PdfRepository pdfRepository;

    @GetMapping
    List<Pdf> all() {
        return pdfRepository.findAll();
    }

}
