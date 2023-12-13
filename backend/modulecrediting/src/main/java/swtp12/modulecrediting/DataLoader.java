package swtp12.modulecrediting;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ModuleBlockCreateDTO;
import swtp12.modulecrediting.dto.ModuleBlockUpdateDTO;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.service.ApplicationService;


/**
 * The DataLoader class is responsible for reading JSON data and saving the data correctly into the database.
 */
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private ModuleLeipzigRepository modulLeipzigRepo;

    @Autowired
    private CourseLeipzigRepository courseLeipzigRepo;

    @Autowired
    private ApplicationService applicationService;

    private final ObjectMapper objectMapper;


    public DataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    @Transactional
    public void run(String... args) {
        String moduleLeipzigData = "/module_liste.json";
        String testData = "/test_data.json";

        leipzigDataLoader(moduleLeipzigData);

        createTestData(testData);
    }


    @Transactional
    private void leipzigDataLoader(String fileName) {
        
        JsonNode courseNodes = grabFirstNodeFromJson(fileName, "courses");
        for (JsonNode courseNode : courseNodes) {
            CourseLeipzig courseLeipzig = new CourseLeipzig(courseNode.get("name").asText());
            if (courseLeipzigRepo.existsById(courseLeipzig.getName()) == false) {
                courseLeipzigRepo.save(courseLeipzig);
            }

            JsonNode modules = grabModulesFromJsonNode(courseNode);
            for (JsonNode module : modules) {
                String name = module.get("name").asText();
                String number = module.get("number").asText();
                ModuleLeipzig moduleLeipzig = new ModuleLeipzig(name, number);
                if (modulLeipzigRepo.existsById(moduleLeipzig.getModuleName()) == false) {
                    modulLeipzigRepo.save(moduleLeipzig);
                }

                CourseLeipzig cLdB = courseLeipzigRepo.findById(courseLeipzig.getName()).get();
                ModuleLeipzig mLdB = modulLeipzigRepo.findById(moduleLeipzig.getModuleName()).get();

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
        System.out.print("Dataloader: Data successfully loaded into Database \n"); 
    }


    @Transactional
    private void createTestData(String testFileName) {

        JsonNode applicationSettingsNode = grabFirstNodeFromJson(testFileName, "randApplications").get(0);
        JsonNode moduleSettingsNode = grabFirstNodeFromJson(testFileName, "randModuleApplications").get(0);
        Random rand = new Random();

        int open = applicationSettingsNode.get("offen").asInt();
        int inEdit = applicationSettingsNode.get("bearbeitung").asInt();
        int closed = applicationSettingsNode.get("fertig").asInt();
        int total = open + inEdit + closed;
        
        List<CourseLeipzig> listOfCourseLeipzig = new ArrayList<>();
        List<CourseLeipzig> listOfCLinDB = courseLeipzigRepo.findAll();
        for (int i = 0; i < total; i++) {
            int randomIdx = rand.nextInt(listOfCLinDB.size());
            CourseLeipzig courseLeipzig = listOfCLinDB.get(randomIdx);
            listOfCourseLeipzig.add(courseLeipzig);
        }
        int count = 1;
        for (CourseLeipzig cL : listOfCourseLeipzig) {
            List<ModuleBlockCreateDTO> listModuleCreateDTO = new ArrayList<>();
            List<ModuleBlockUpdateDTO> listModuleUpdateDTO = new ArrayList<>();
            ApplicationCreateDTO applicationCreateDTO = new ApplicationCreateDTO();
            applicationCreateDTO.setCourseLeipzig(cL.getName());

            int rIdx = rand.nextInt(3) + 1;
            for (int i = 0; i < rIdx; i++) {
                ModuleBlockCreateDTO mBcDTO = createModuleDTO(cL, moduleSettingsNode);
                mBcDTO.setModuleName(mBcDTO.getModuleName() + "_" + count);
                mBcDTO.setUniversity(mBcDTO.getUniversity() + "_" + count);
                listModuleCreateDTO.add(mBcDTO);
                ModuleBlockUpdateDTO mUdto = new ModuleBlockUpdateDTO();
                listModuleUpdateDTO.add(mUdto);
                count++;
            }

            applicationCreateDTO.setCourseLeipzig(cL.getName());
            applicationCreateDTO.setModuleBlockCreateDTOList(listModuleCreateDTO);

            String vorgangsnummer = applicationService.createApplication(applicationCreateDTO);
            System.out.println("Created Dummy Application: " + vorgangsnummer);
        }
    }

    //Helper methods:

    /**
     * The function "grabFirstNodeFromJson" reads a JSON file from the given path and returns the first JsonNode
     * with the given name or throws mutliple exception if failed to do so.
     * 
     * @exception IOException Gets thrown as RuntimeException if failed to read JSON data
     * @exception IllegalArgumentException Gets thrown if JsonNode is invalid
     * @param jsonPath String that represents the path to the JSON file that you want to read. 
     *                      It can be either an absolute path or a relative path to the JSON file.
     * @param nodeName String that represents the Name of the JsonNode you want to be returned
     * @return The JsonNode of the JSON file.
     */
    private JsonNode grabFirstNodeFromJson(String jsonPath, String nodeName) {
        JsonNode jsonNode;
        try (InputStream inputStream = TypeReference.class.getResourceAsStream(jsonPath)) {
            jsonNode = objectMapper.readValue(inputStream, JsonNode.class);
        } 
        catch (IOException e) { throw new RuntimeException("Failed to read JSON data", e); }

        return Optional.ofNullable(jsonNode)
                .map(j -> j.get(nodeName))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
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


    private ModuleBlockCreateDTO createModuleDTO(CourseLeipzig cL, JsonNode moduleSettingNode) {
        ModuleBlockCreateDTO moduleBlockCreateDTO = new ModuleBlockCreateDTO();
        Random rdm = new Random();
        moduleBlockCreateDTO.setModuleName(moduleSettingNode.get("name").asText());
        moduleBlockCreateDTO.setUniversity(moduleSettingNode.get("uni").asText());
        moduleBlockCreateDTO.setPoints(Integer.parseInt(getRandValueOfNode(moduleSettingNode, "points", rdm)));
        moduleBlockCreateDTO.setPointSystem(getRandValueOfNode(moduleSettingNode, "pointSystem", rdm));
        moduleBlockCreateDTO.setCommentApplicant(getRandValueOfNode(moduleSettingNode, "comment", rdm));
        
        List<String> listModuleLeipzig = new ArrayList<>();
        int numberOf = rdm.nextInt(3)+1;
        for (int i = 0; i < numberOf; i++) {
            int idxR = rdm.nextInt(cL.getModulesLeipzigCourse().size());
            listModuleLeipzig.add(cL.getModulesLeipzigCourse().get(idxR).getModuleName());
        }
        moduleBlockCreateDTO.setModuleNamesLeipzig(listModuleLeipzig);


        // Get the class loader to access resources on the classpath
        Path pdfPath = Paths.get("dummy.pdf");
        ClassLoader classLoader = DataLoader.class.getClassLoader();
        URL resource = classLoader.getResource(pdfPath.toString());

        // Check if the resource (file) exists
        if (resource != null) {
            try { // Read the file content
                Path filePath = Paths.get(resource.toURI());
                byte[] pdf = Files.readAllBytes(filePath);
                MultipartFile pdfMultipartFile = new MockMultipartFile("dummy", "dummy.pdf", "application/pdf", pdf);
                moduleBlockCreateDTO.setDescription(pdfMultipartFile);

            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException("Failed to read dummy.pdf", e);
            }
        } else {
            System.out.println("File not found: " + pdfPath);
            MultipartFile pdfMultipartFile = new MockMultipartFile("dummy", "dummy.pdf", "application/pdf", "pdf_data_mock".getBytes());
            moduleBlockCreateDTO.setDescription(pdfMultipartFile);
        }

        return moduleBlockCreateDTO;
    }



    private String getRandValueOfNode(JsonNode currentNode, String nodeName, Random rdm) {
        JsonNode valueNode = currentNode.get(nodeName);
        int rdmIdx = rdm.nextInt(valueNode.size());
        return valueNode.get(rdmIdx).asText();
    }
}