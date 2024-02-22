package swtp12.modulecrediting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import swtp12.modulecrediting.dto.LeipzigDataDTO;
import swtp12.modulecrediting.service.JsonLeipzigDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/file/json")
public class JsonFileController {
    @Autowired
    private JsonLeipzigDataService jsonLeipzigDataService;
    
    @GetMapping("/courses")
    public ResponseEntity<LeipzigDataDTO> getAllLeipzigData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fileName = "modulecrediting_config_" + LocalDateTime.now().format(formatter) + ".json";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(jsonLeipzigDataService.getAllLeipzigData());
    }

    @PostMapping("/courses/upload")
    public String postMethodName(@RequestParam("jsonFile")MultipartFile multipartFile) {
        jsonLeipzigDataService.uploadData(multipartFile);
        return String.format("File %s uploaded successfully", multipartFile.getOriginalFilename());
    }
    
}
