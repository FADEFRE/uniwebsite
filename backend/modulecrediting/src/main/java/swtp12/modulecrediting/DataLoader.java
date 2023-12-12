package swtp12.modulecrediting;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;


/**
 * The DataLoader class is responsible for reading JSON data and saving the data correctly into the database.
 */
@Component
@Transactional
public class DataLoader implements CommandLineRunner {
    @Autowired
    private ModuleLeipzigRepository modulLeipzigRepo;

    @Autowired
    private CourseLeipzigRepository courseLeipzigRepo;

    private final ObjectMapper objectMapper;


    public DataLoader(ObjectMapper objectMapper, ModuleLeipzigRepository modulLeipzigRepo, CourseLeipzigRepository courseLeipzigRepo) {
        this.objectMapper = objectMapper;
        this.modulLeipzigRepo = modulLeipzigRepo;
        this.courseLeipzigRepo = courseLeipzigRepo;
    }

    //master method

    /**
     * The 'run' function reads JSON data, creates courses and modules from the data, checks for
     * duplicate modules, establishes relations between courses and modules, and saves the data into a
     * database.
     */
    @Override
    public void run(String... args) {
        String jsonPath = "/module_liste.json";

        System.out.print("Dataloader: Trying to read JSON");
        JsonNode courseNodes = grabCoursesFromJson(jsonPath);

        System.out.print("\033[2K\033[1G");
        System.out.print("Dataloader: Grabbing data from JSON");

        for (JsonNode courseNode : courseNodes) {
            System.out.print("\033[2K\033[1G");
            System.out.print("Dataloader: Creating a course");

            CourseLeipzig cL = createCourseFromNode(courseNode);
            if (courseLeipzigRepo.existsById(cL.getName()) == false) {
                courseLeipzigRepo.save(cL);
            }
            
            JsonNode modules = grabModulesFromJsonNode(courseNode);
            
            System.out.print("\033[2K\033[1G");
            System.out.print("Dataloader: Creating modules for this course");

            for (JsonNode module : modules) {
                ModuleLeipzig mL = createModulsFromNode(module);
                if (modulLeipzigRepo.existsById(mL.getModuleName()) == false) {
                    modulLeipzigRepo.save(mL);
                }
                CourseLeipzig cLdB = courseLeipzigRepo.findById(cL.getName()).get();
                ModuleLeipzig mLdB = modulLeipzigRepo.findById(mL.getModuleName()).get();

                List<ModuleLeipzig> mList = cLdB.getModulesLeipzigCourse(); ;
                mList.add(mLdB);
                cLdB.setModulesLeipzigCourse(mList);

                List<CourseLeipzig> cList = mLdB.getCoursesLeipzig();
                cList.add(cLdB);
                mLdB.setCoursesLeipzig(cList);

                courseLeipzigRepo.save(cLdB);
                modulLeipzigRepo.save(mLdB);
            }
        }
        System.out.print("\033[2K\033[1G");
        System.out.print("Dataloader: Data successfully loaded into Database"); 
    }


    //Helper methods:

    /**
     * The function "grabCoursesFromJson" reads a JSON file from the given path and returns the 'courses' JsonNode 
     * or throws mutliple exception if failed to do so.
     * 
     * @exception IOException Gets thrown as RuntimeException if failed to read JSON data
     * @exception IllegalArgumentException Gets thrown if JsonNode is invalid
     * @param jsonPath String that represents the path to the JSON file that you want to read. 
     *                      It can be either an absolute path or a relative path to the JSON file.
     * @return The 'courses' JsonNode of the JSON file.
     */
    private JsonNode grabCoursesFromJson(String jsonPath) {
        JsonNode jsonNode;
        try (InputStream inputStream = TypeReference.class.getResourceAsStream(jsonPath)) {
            jsonNode = objectMapper.readValue(inputStream, JsonNode.class);
        } 
        catch (IOException e) { throw new RuntimeException("Failed to read JSON data", e); }

        return Optional.ofNullable(jsonNode)
                .map(j -> j.get("courses"))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }


    /**
     * The function creates a new 'CourseLeipzig' object with the name extracted from the 'course' JsonNode and adds it
     * to the list of all courses.
     * 
     * @param course A JsonNode object representing a course.
     */
    private CourseLeipzig createCourseFromNode(JsonNode course) {
        String courseName = course.get("name").asText();
        CourseLeipzig cL = new CourseLeipzig(courseName);
        //coursesInLeipzig.add(cL);
        return cL;
    }


    /**
     * The function grabs the "modules" field from the 'courses' JsonNode and returns it as a JsonNode object 
     * or throws an exception if object invalid.
     * 
     * @exception IllegalArgumentException Gets thrown if JsonNode is invalid
     * @param courseNode A JsonNode object representing a course.
     * @return The 'modules' JsonNode of the 'courseNode'
     */
    private JsonNode grabModulesFromJsonNode(JsonNode courseNode) {
        return Optional.ofNullable(courseNode)
        .map(c -> c.get("modules"))
        .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }


    /**
     * The function creates a 'ModuleLeipzig' object from the 'module' JsonNode and 
     *  - adds it to a list of modules for that course.
     *  - adds that course to the list of courses for this module.
     * 
     * @param module A JsonNode representing a module.
     * @param course A JsonNode object representing a course.
     */
    private ModuleLeipzig createModulsFromNode(JsonNode module) {
        String name = module.get("name").asText();
        String number = module.get("number").asText();
        ModuleLeipzig mLeipzig = new ModuleLeipzig(name, number);
        return mLeipzig;
    }

}