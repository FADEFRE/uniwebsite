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

/**
 * This is a {@code Service} for uploading and downloading JSON
 * @author Frederik Kluge
 * @see #getAllLeipzigData
 * @see #uploadData
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Spring Service</a>
 */
@Service
public class JsonLeipzigDataService {
    @Autowired
    private CourseLeipzigService courseLeipzigService;
    @Autowired
    private ModuleLeipzigService moduleLeipzigService;

    /**
     * This method gets all {@link CourseLeipzig} and {@link ModuleLeipzig} in the database
     * @return {@link LeipzigDataDTO} to be sent as JSON
     * @see CourseLeipzig
     * @see LeipzigDataDTO
     * @see ModuleLeipzig
     */
    public LeipzigDataDTO getAllLeipzigData() {
        LeipzigDataDTO leipzigDataDTO = new LeipzigDataDTO();

        List<CourseLeipzig> cLeipzig = getAllActiveCourseLeipzig();
        List<LeipzigDataCourseDTO> courseList = new ArrayList<>();

        for (CourseLeipzig courseLeipzig : cLeipzig) {
            LeipzigDataCourseDTO leipzigDataCourseDTO = new LeipzigDataCourseDTO();
            leipzigDataCourseDTO.setName(courseLeipzig.getName());
            List<ModuleLeipzigDTO> modulesList = new ArrayList<>();
            
            List<ModuleLeipzig> mLeipzig = getAllActiveModuleLeipzigFromCourse(courseLeipzig);
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

    /**
     * This method uploads all {@link CourseLeipzig} and {@link ModuleLeipzig} definied in the {@code MultipartFile}
     * <p> NOTE: This method overwrites all {@link CourseLeipzig} and {@link ModuleLeipzig} data in the database. Use with caution!!
     * @param multipartFile {@code MultipartFile}
     * @see CourseLeipzig
     * @see ModuleLeipzig
     * @see JsonUtil
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html">Spring MultipartFile</a> 
     */
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
    

    // ------- Private Methods -------

    /**
     * This method returns all active {@link CourseLeipzig} in the database
     * @return {@code List} of all active {@link CourseLeipzig}
     */
    private List<CourseLeipzig> getAllActiveCourseLeipzig() {
        List<CourseLeipzig> activeCourses = new ArrayList<>();
        List<CourseLeipzig> courses = courseLeipzigService.getAllCoursesLeipzig();
        for (CourseLeipzig courseLeipzig : courses) {
            if (courseLeipzig.getIsActive()) {
                activeCourses.add(courseLeipzig);
            }
        }
        return activeCourses;
    }

    /**
     * This method returns all active {@link ModuleLeipzig} in a given {@link CourseLeipzig}
     * @param courseLeipzig {@link CourseLeipzig}
     * @return {@code List} of all active {@link ModuleLeipzig} in the given {@link CourseLeipzig}
     */
    private List<ModuleLeipzig> getAllActiveModuleLeipzigFromCourse(CourseLeipzig courseLeipzig) {
        List<ModuleLeipzig> activeModules = new ArrayList<>();
        List<ModuleLeipzig> modules = courseLeipzig.getModulesLeipzigCourse();
        for (ModuleLeipzig moduleLeipzig : modules) {
            if (moduleLeipzig.getIsActive()) {
                activeModules.add(moduleLeipzig);
            }
        }
        return activeModules;
    }


    /**
     * This method returns a {@code List} of {@code Long} containing the {@code ids} of all the {@link CourseLeipzig} in the database
     * @return {@code List} of {@code id} of the {@link CourseLeipzig}
     * @see {@link CourseLeipzig}
     */
    private List<Long> getAllCourseIds() {
        List<Long> courseIds = new ArrayList<>();
        List<CourseLeipzig> courses = courseLeipzigService.getAllCoursesLeipzig();
        for (CourseLeipzig courseLeipzig : courses) {
            courseIds.add(courseLeipzig.getId());
        }
        return courseIds;
    }

    /**
     * This method returns a {@code List} of {@code Long} containing the {@code ids} of all the {@link ModuleLeipzig} in the database
     * @return {@code List} of {@code id} of the {@link ModuleLeipzig}
     * @see {@link ModuleLeipzig}
     */
    private List<Long> getAllModuleIds() {
        List<Long> moduelIds = new ArrayList<>();
        List<ModuleLeipzig> modules = moduleLeipzigService.getAllModulesLeipzig();
        for (ModuleLeipzig moduleLeipzig : modules) {
            moduelIds.add(moduleLeipzig.getId());
        }
        return moduelIds;
    }

    /**
     * This method removes all {@link CourseLeipzig} and {@link ModuleLeipzig} definied by their {@code ids} from the database
     * @param courseIds {@code List} of {@code CourseLeipzig id}
     * @param moduleIds {@code List} of {@code ModuleLeipzig id}
     * @see CourseLeipzig
     * @see CourseLeipzigService
     * @see ModuleLeipzig
     * @see ModuleLeipzigService
     */
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
