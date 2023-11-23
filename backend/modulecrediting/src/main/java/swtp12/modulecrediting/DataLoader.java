package swtp12.modulecrediting;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final ModuleLeipzigRepository modulLeipzigRepo;
    private final CourseLeipzigRepository courseLeipzigRepo;

    public DataLoader(ObjectMapper objectMapper, ModuleLeipzigRepository modulLeipzigRepo, CourseLeipzigRepository courseLeipzigRepo) {
        this.objectMapper = objectMapper;
        this.modulLeipzigRepo = modulLeipzigRepo;
        this.courseLeipzigRepo = courseLeipzigRepo;
    }

    @Override
    public void run(String... args) {
        List<ModuleLeipzig> modulsLeipzigsSorted = new ArrayList<>();
        List<CourseLeipzig> courseLeipzigs = new ArrayList<>();
        JsonNode json;

        //read in json and get the first node
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/module_liste.json")) {
            json = objectMapper.readValue(inputStream, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data", e);
        }

        //grab and create courses and modules form first node
        JsonNode courses = getCourses(json);
        for (JsonNode course : courses) {
            List<ModuleLeipzig> modulesForCourse = new ArrayList<>();
            String courseName = course.get("name").asText();
            CourseLeipzig cL = createCourseFromNode(courses, courseName);
            courseLeipzigs.add(cL);
            JsonNode modules = course.get("modules");
            for (JsonNode modul : modules) {
                ModuleLeipzig mLeipzig = createModulsFromNode(modul, cL);
                modulesForCourse.add(mLeipzig);
            } 
            modulsLeipzigsSorted = removeDuplicateModules(modulsLeipzigsSorted, modulesForCourse);
        }

        for (CourseLeipzig courseDB : courseLeipzigs) {
            String courseName = courseDB.getName();
            for (ModuleLeipzig moduleDB : modulsLeipzigsSorted) {
                List<String> coursesInModules = moduleDB.getDataloaderOnlyCourses();
                if (coursesInModules.contains(courseName)) {
                    courseDB.addCourseToModulesLeipzig(moduleDB);
                }
            }
        }
        courseLeipzigRepo.saveAll(courseLeipzigs);
        modulLeipzigRepo.saveAll(modulsLeipzigsSorted);
        System.out.println("Dataloader: Data successfully loaded into Database");        
        System.out.println("Dataloader: Relations successfully established");
        
    }

    private List<ModuleLeipzig> removeDuplicateModules(List<ModuleLeipzig> modulesLeipzigsSorted, List<ModuleLeipzig> modulesForCourse) {
        for (ModuleLeipzig moduleForCourse : modulesForCourse) {
            if (modulesLeipzigsSorted.isEmpty()) {
                modulesLeipzigsSorted.add(moduleForCourse);
            }
            else {
                String moduleCode = moduleForCourse.getModuleCode();
                String moduleName = moduleForCourse.getModuleName();
                int exists = 0;
                for (int i = 0; i < modulesLeipzigsSorted.size(); i++) {
                    String moduleCodeSort = modulesLeipzigsSorted.get(i).getModuleCode();
                    String moduleNameSort = modulesLeipzigsSorted.get(i).getModuleName();                                
                    if (moduleCode.equals(moduleCodeSort) && moduleName.equals(moduleNameSort)) {
                        modulesLeipzigsSorted.get(i).addDataloaderOnlyCourses(moduleForCourse.getDataloaderOnlyCourses());
                        exists++;
                    }
                }
                if (exists == 0) {
                    modulesLeipzigsSorted.add(moduleForCourse);
                }
            }
        }
        return modulesLeipzigsSorted;
    }

    //helper to get courses
    private JsonNode getCourses(JsonNode json) {
        return Optional.ofNullable(json)
                .map(j -> j.get("courses"))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }

    //helper to create Modules from courses node
    private ModuleLeipzig createModulsFromNode(JsonNode modul, CourseLeipzig cL) {
        String name = modul.get("name").asText();
        String number = modul.get("number").asText();
        List<String> courseNames = new ArrayList<String>();
        String courseName = cL.getName();
        courseNames.add(courseName);
        return new ModuleLeipzig(name, number, courseNames);
    }

    //helper to create Course form courses node
    private CourseLeipzig createCourseFromNode(JsonNode course, String courseName) {
        return new CourseLeipzig(courseName);
    }

}