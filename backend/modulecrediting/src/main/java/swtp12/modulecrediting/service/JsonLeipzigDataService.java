package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

import swtp12.modulecrediting.dto.CourseLeipzigRelationEditDTO;
import swtp12.modulecrediting.dto.LeipzigDataCourseDTO;
import swtp12.modulecrediting.dto.LeipzigDataDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.util.JsonUtil;
import swtp12.modulecrediting.util.LogUtil;

@Service
public class JsonLeipzigDataService {
    @Autowired
    private CourseLeipzigService courseLeipzigService;
    @Autowired
    private ModuleLeipzigService moduleLeipzigService;


    public LeipzigDataDTO getAllLeipzigData() {
        LeipzigDataDTO leipzigDataDTO = new LeipzigDataDTO();

        List<CourseLeipzig> cLeipzig = courseLeipzigService.getAllCoursesLeipzig();
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
        JsonNode coursesNode = JsonUtil.grabJsonNodeFromMultipartFile(multipartFile, "courses");
        List<Long> courseIdsToRemove = getAllCourseIds();
        List<Long> moduleIdsToRemove = getAllModuleIds();
        LogUtil.printLog("--- Uploaded JSON Data. Starting editing of data: ---");
        for (JsonNode course : coursesNode) {
            LogUtil.printLog("");
            String courseName = course.get("name").asText();
            CourseLeipzig courseLeipzig = courseLeipzigService.findOrCreateNewCourseLeipzig(courseName);
            courseIdsToRemove.remove(courseLeipzig.getId());
            courseLeipzig.removeModulesLeipzig();
            List<ModuleLeipzigDTO> moduleLeipzigDTOs = new ArrayList<>();

            JsonNode modulesNode = JsonUtil.grabJsonNodeFromJsonNode(course, "modules");
            for (JsonNode module : modulesNode) {
                String moduleName = module.get("name").asText();
                String moduleCode = module.get("code").asText();
                ModuleLeipzig moduleLeipzig = moduleLeipzigService.findOrCreateNewModuleLeipzig(moduleName, moduleCode);
                moduleIdsToRemove.remove(moduleLeipzig.getId());
                moduleLeipzigDTOs.add(new ModuleLeipzigDTO(moduleLeipzig.getName(), moduleLeipzig.getCode()));
            }

            courseLeipzigService.editCourseRelations(courseLeipzig.getName(), new CourseLeipzigRelationEditDTO(moduleLeipzigDTOs));
            courseLeipzigService.saveCourseLeipzigToDatabase(courseLeipzig);
        }
        removeAllNonUploaded(courseIdsToRemove, moduleIdsToRemove);
        LogUtil.printLog("");
        LogUtil.printLog("--- Finished editing of data provided by uploaded JSON ---");
    }
    

    private List<Long> getAllCourseIds() {
        List<Long> courseIds = new ArrayList<>();
        List<CourseLeipzig> courses = courseLeipzigService.getAllCoursesLeipzig();
        for (CourseLeipzig courseLeipzig : courses) {
            courseIds.add(courseLeipzig.getId());
        }
        return courseIds;
    }

    private List<Long> getAllModuleIds() {
        List<Long> moduelIds = new ArrayList<>();
        List<ModuleLeipzig> modules = moduleLeipzigService.getAllModulesLeipzig();
        for (ModuleLeipzig moduleLeipzig : modules) {
            moduelIds.add(moduleLeipzig.getId());
        }
        return moduelIds;
    }

    private void removeAllNonUploaded(List<Long> courseIds, List<Long> moduleIds) {
        for (Long courseId : courseIds) {
            String courseName = courseLeipzigService.getCourseLeipzigNameById(courseId);
            CourseLeipzig courseLeipzig = courseLeipzigService.getCourseLeipzigByName(courseName);
            if (courseLeipzig.getIsActive()) {
                courseLeipzigService.deleteCourseLeipzig(courseName);
            }
        }

        for (Long moduleId : moduleIds) {
            String moduleName = moduleLeipzigService.getModuleLeipzigNameById(moduleId);
            ModuleLeipzig moduleLeipzig = moduleLeipzigService.getModuleLeipzigByName(moduleName);
            if (moduleLeipzig.getIsActive()) {
                moduleLeipzigService.deleteModuleLeipzig(moduleName);
            }
        }
    }

}
