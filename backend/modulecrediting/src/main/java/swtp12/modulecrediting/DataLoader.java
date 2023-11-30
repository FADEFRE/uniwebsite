package swtp12.modulecrediting;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;


/**
 * The DataLoader class is responsible for reading JSON data and saving the data correctly into the database.
 */
@Component
public class DataLoader implements CommandLineRunner {
    
    private final ObjectMapper objectMapper;
    private final ModuleLeipzigRepository modulLeipzigRepo;
    private final CourseLeipzigRepository courseLeipzigRepo;
    
    private List<CourseLeipzig> coursesInLeipzig = new ArrayList<>();
    private List<ModuleLeipzig> modulesLeipzigsSorted = new ArrayList<>();


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

            createCourseFromNode(courseNode);
            List<ModuleLeipzig> modulesForCourse = new ArrayList<>();
            JsonNode modules = grabModulesFromJsonNode(courseNode);

            System.out.print("\033[2K\033[1G");
            System.out.print("Dataloader: Creating modules for this course");

            for (JsonNode module : modules) {
                ModuleLeipzig mL = createModulsFromNode(module, courseNode);
                modulesForCourse.add(mL);
            }

            System.out.print("\033[2K\033[1G");
            System.out.print("Dataloader: Checking for duplicate modules for this course");

            removeDuplicateModules(modulesForCourse);
            modulesForCourse.clear();
        }

        for (CourseLeipzig courseDB : coursesInLeipzig) { addModulesToCourseDtL(courseDB); }

        System.out.print("\033[2K\033[1G");
        System.out.print("Dataloader: Relations successfully established");
        System.out.print("Dataloader: Writing Data into Database");

        courseLeipzigRepo.saveAll(coursesInLeipzig);
        modulLeipzigRepo.saveAll(modulesLeipzigsSorted);
        coursesInLeipzig.clear();
        modulesLeipzigsSorted.clear();

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
    private void createCourseFromNode(JsonNode course) {
        String courseName = course.get("name").asText();
        CourseLeipzig cL = new CourseLeipzig(courseName);
        coursesInLeipzig.add(cL);
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
    private ModuleLeipzig createModulsFromNode(JsonNode module, JsonNode course) {
        String courseName = course.get("name").asText();
        String name = module.get("name").asText();
        String number = module.get("number").asText();
        List<String> allCourseNames = new ArrayList<String>();
        allCourseNames.add(courseName);
        ModuleLeipzig mLeipzig = new ModuleLeipzig(name, number, allCourseNames);
        return mLeipzig;
    }


    /**
     * The function only adds non-duplicate 'modules' from a List of modules for a 'course' to the 'List' of all modules.
     * The function calls the helper-function {@link #removeDupeModHelper()}
     * 
     * @param modulesForCourse A list of ModuleLeipzig objects representing the modules for a course.
     */
    private void removeDuplicateModules(List<ModuleLeipzig> modulesForCourse) {
        for (ModuleLeipzig moduleOfCourse : modulesForCourse) {
            boolean exists = false;
            if (!modulesLeipzigsSorted.isEmpty()) { exists = removeDupeModHelper(moduleOfCourse);}
            if (exists == false) { modulesLeipzigsSorted.add(moduleOfCourse); }
        }
    }


    /**
     * The helper-function checks if a 'module' already exists in a list and 
     * if so, adds given 'course' to the already existing 'module' in the list 'modulesLeipzigsSorted'.
     * 
     * @param moduleOfCourse A 'ModuleLeipzig' object, that needs to be checked for duplication.
     * @return true if the 'module' already existed.
     */
    private boolean removeDupeModHelper(ModuleLeipzig moduleOfCourse) {
        String moduleCode = moduleOfCourse.getModuleCode();
        String moduleName = moduleOfCourse.getModuleName();
        boolean bool = false;
        for (int i = 0; i < modulesLeipzigsSorted.size(); i++) {
            String moduleCodeSort = modulesLeipzigsSorted.get(i).getModuleCode();
            String moduleNameSort = modulesLeipzigsSorted.get(i).getModuleName();                                
            if (moduleCode.equals(moduleCodeSort) && moduleName.equals(moduleNameSort)) {
                //if module already exists: only add the new Course to the Module already in the List
                List<String> mDtLC = moduleOfCourse.getDataloaderOnlyCourses();
                modulesLeipzigsSorted.get(i).addDataloaderOnlyCourses(mDtLC);
                bool = true;
            }
        }
        return bool;
    }


    /**
     * The function adds all modules, that belong to the giving 'course', to it.
     * 
     * @param course The 'course' to which the modules should be added
     */
    private void addModulesToCourseDtL(CourseLeipzig course) {
        String courseName = course.getName();
        for (ModuleLeipzig moduleDB : modulesLeipzigsSorted) {
            if (moduleDB.getDataloaderOnlyCourses().contains(courseName)) {
                course.addCourseToModulesLeipzig(moduleDB);
            }
        }
    }


}