package swtp12.modulecrediting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import swtp12.modulecrediting.service.JsonLeipzigDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/file/json")
public class JsonFileController {
    @Autowired
    private JsonLeipzigDataService jsonLeipzigDataService;
    
    @GetMapping("/courses")
    public String getAllSa(@RequestParam String param) {
        
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //TODO: process GET request
        return new String();
    }

    @PostMapping("/courses/upload")
    public String postMethodName(@RequestParam("jsonFile")MultipartFile multipartFile) {
        jsonLeipzigDataService.uploadData(multipartFile);
        return String.format("File %s uploaded successfully", multipartFile.getOriginalFilename());
    }
    
}
