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
        List<ModuleLeipzig> fullModulsLeipzigs = new ArrayList<>();
        List<ModuleLeipzig> modulsLeipzigs = new ArrayList<>();
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
            String courseName = course.get("name").asText();
            CourseLeipzig cL = createCourseFromNode(courses, courseName);
            courseLeipzigs.add(cL);
            JsonNode modules = course.get("modules");
            for (JsonNode modul : modules) {
                ModuleLeipzig mLeipzig = createModulsFromNode(modul, courseLeipzigs);
                modulsLeipzigs.add(mLeipzig);
            }
            for (ModuleLeipzig mL : modulsLeipzigs) {
                ModuleLeipzig doubleTester = mL;
                for (ModuleLeipzig fullModule : fullModulsLeipzigs) {
                    if (fullModule.getModuleCode().equals(mL.getModuleCode())) {
                        doubleTester = fullModule;
                    }
                }
                fullModulsLeipzigs.add(doubleTester);
                cL.addCourseToModulesLeipzig(doubleTester);
            }
            modulsLeipzigs.clear();
        }
        courseLeipzigRepo.saveAll(courseLeipzigs);
        modulLeipzigRepo.saveAll(fullModulsLeipzigs);
        System.out.println("Dataloader: Data successfully loaded into Database");        
        System.out.println("Dataloader: Relations successfully established");

    }

    //helper to get courses
    private JsonNode getCourses(JsonNode json) {
        return Optional.ofNullable(json)
                .map(j -> j.get("courses"))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }

    //helper to create Modules from courses node
    private ModuleLeipzig createModulsFromNode(JsonNode modul, List<CourseLeipzig> courseLeipzigs) {
        String name = modul.get("name").asText();
        String number = modul.get("number").asText();
        return new ModuleLeipzig(name, number);
    }

    //helper to create Course form courses node
    private CourseLeipzig createCourseFromNode(JsonNode course, String courseName) {
        return new CourseLeipzig(courseName);
    }

}