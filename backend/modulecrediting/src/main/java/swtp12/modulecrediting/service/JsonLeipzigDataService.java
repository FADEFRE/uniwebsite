package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.util.JsonUtil;

@Service
public class JsonLeipzigDataService {
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private CourseLeipzigRepository courseLeipzigRepository;
    @Autowired
    private ModuleLeipzigRepository moduleLeipzigRepository;

    public void uploadData(MultipartFile multipartFile) {

        JsonNode coursesNode = jsonUtil.grabJsonNodeFromMultipartFile(multipartFile, "courses");
        for (JsonNode course : coursesNode) {
            String courseName = course.get("name").asText();
            CourseLeipzig courseLeipzig = courseLeipzigRepository.findByName(courseName)
                    .orElseGet(() -> {
                        return courseLeipzigRepository.save(new CourseLeipzig(courseName));
                    });

            JsonNode modulesNode = jsonUtil.grabJsonNodeFromJsonNode(course, "modules");
            for (JsonNode module : modulesNode) {
                String moduleName = module.get("name").asText();
                String moduleCode = module.get("number").asText();
                ModuleLeipzig moduleLeipzig = moduleLeipzigRepository.findByName(moduleName)
                        .orElseGet(() -> {
                            return moduleLeipzigRepository.save(new ModuleLeipzig(moduleName, moduleCode));
                        });

                courseLeipzig.addModulesLeipzig(moduleLeipzig);
                courseLeipzigRepository.save(courseLeipzig);
            }
        }
    }
    
}
