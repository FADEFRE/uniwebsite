package swtp12.modulecrediting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import swtp12.modulecrediting.dto.LeipzigDataDTO;
import swtp12.modulecrediting.service.JsonLeipzigDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/file/json")
public class JsonFileController {
    @Autowired
    private JsonLeipzigDataService jsonLeipzigDataService;
    
    @GetMapping("/courses")
    public ResponseEntity<LeipzigDataDTO> getAllLeipzigData() {
        return ResponseEntity.ok(jsonLeipzigDataService.getAllLeipzigData());
    }

    @PostMapping("/courses/upload")
    public String postMethodName(@RequestParam("jsonFile")MultipartFile multipartFile) {
        jsonLeipzigDataService.uploadData(multipartFile);
        return String.format("File %s uploaded successfully", multipartFile.getOriginalFilename());
    }
    
}
