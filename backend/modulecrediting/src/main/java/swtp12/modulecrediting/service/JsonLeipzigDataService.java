package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

import swtp12.modulecrediting.dto.LeipzigDataCourseDTO;
import swtp12.modulecrediting.dto.LeipzigDataDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
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


    public LeipzigDataDTO getAllLeipzigData() {
        LeipzigDataDTO leipzigDataDTO = new LeipzigDataDTO();

        List<CourseLeipzig> cLeipzig = courseLeipzigRepository.findAll();
        List<LeipzigDataCourseDTO> courseList = new ArrayList<>();

        for (CourseLeipzig courseLeipzig : cLeipzig) {
            LeipzigDataCourseDTO leipzigDataCourseDTO = new LeipzigDataCourseDTO();
            leipzigDataCourseDTO.setName(courseLeipzig.getName());
            List<ModuleLeipzigDTO> modulesList = new ArrayList<>();
            
            List<ModuleLeipzig> mLeipzig = courseLeipzig.getModulesLeipzigCourse();
            for (ModuleLeipzig moduleLeipzig : mLeipzig) {
                ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
                moduleLeipzigDTO.setName(moduleLeipzig.getName());
                moduleLeipzigDTO.setCode(moduleLeipzig.getCode());
                modulesList.add(moduleLeipzigDTO);
            }

            leipzigDataCourseDTO.setModules(modulesList);
            courseList.add(leipzigDataCourseDTO);
        }

        leipzigDataDTO.setCourses(courseList);
        return leipzigDataDTO;
    }





    public void uploadData(MultipartFile multipartFile) {

        JsonNode coursesNode = jsonUtil.grabJsonNodeFromMultipartFile(multipartFile, "courses");
        for (JsonNode course : coursesNode) {
            String courseName = course.get("name").asText();
            CourseLeipzig courseLeipzig = courseLeipzigRepository.findByName(courseName)
                    .orElseGet(() -> {
                        return courseLeipzigRepository.save(new CourseLeipzig(courseName));
                    });
            courseLeipzig.removeModulesLeipzig();

            JsonNode modulesNode = jsonUtil.grabJsonNodeFromJsonNode(course, "modules");
            for (JsonNode module : modulesNode) {
                String moduleName = module.get("name").asText();
                String moduleCode = module.get("code").asText();
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
