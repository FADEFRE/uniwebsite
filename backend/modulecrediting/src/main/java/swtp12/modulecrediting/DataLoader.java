package swtp12.modulecrediting;

import static swtp12.modulecrediting.model.EnumApplicationStatus.*;
import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;

import java.io.IOException;
import java.io.InputStream;
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
import swtp12.modulecrediting.dto.*;
import swtp12.modulecrediting.model.*;
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

    /**
     * The run function hooks into the run function of the main. 
     * Reads Uni Leipzig Data from a JSON and writes it into the database
     * Reads test data from a Json and writes it into the databse
     */
    @Override
    @Transactional
    public void run(String... args) {
        String moduleLeipzigData = "/module_liste.json";
        String testData = "/test_data.json";

        leipzigDataLoader(moduleLeipzigData);

        createTestData(testData);
    }


    //the two dataloader functions (leipzigData/ testData):

    /**
     * The function `leipzigDataLoader` loads data from a JSON file into the database, creating and linking
     * `CourseLeipzig` and `ModuleLeipzig` objects.
     * 
     * @param fileName The `fileName` parameter is the name of the JSON file that contains the data to be
     * loaded into the database.
     */
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
                if (modulLeipzigRepo.existsById(moduleLeipzig.getName()) == false) {
                    modulLeipzigRepo.save(moduleLeipzig);
                }

                CourseLeipzig cLdB = courseLeipzigRepo.findById(courseLeipzig.getName()).get();
                ModuleLeipzig mLdB = modulLeipzigRepo.findById(moduleLeipzig.getName()).get();

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
        System.out.println("Dataloader: Data successfully loaded into Database \n"); 
    }

    /**
     * The `createTestData` function generates dummy data for testing purposes by creating random
     * applications with associated module blocks.
     * 
     * @param testFileName The `testFileName` parameter is the name of the JSON file from which the test
     * data settings will be extracted.
     */

    @Transactional
    private void createTestData(String testFileName) {
        //Loads specified courses/modules
        //leipzigDataLoader(testFileName);
        //System.out.print("\033[2K\033[1G");

        JsonNode applicationSettingsNode = grabFirstNodeFromJson(testFileName, "randApplications").get(0);
        JsonNode moduleSettingsNode = grabFirstNodeFromJson(testFileName, "randModuleApplications").get(0);
        Random rand = new Random();

        int open = applicationSettingsNode.get("new").asInt();
        int studyOffice = applicationSettingsNode.get("study-office").asInt();
        int pav = applicationSettingsNode.get("pav").asInt();
        int closed = applicationSettingsNode.get("closed").asInt();
        int total = open + studyOffice + pav + closed;
        
        List<CourseLeipzig> listOfCourseLeipzig = new ArrayList<>();
        List<CourseLeipzig> listOfCLinDB = courseLeipzigRepo.findAll();
        for (int i = 0; i < total; i++) {
            int randomIdx = rand.nextInt(listOfCLinDB.size());
            CourseLeipzig courseLeipzig = listOfCLinDB.get(randomIdx);
            listOfCourseLeipzig.add(courseLeipzig);
        }
        
        System.out.println("Dataloader: Generating random Dummy Applications:");
        for (CourseLeipzig cL : listOfCourseLeipzig) {
            List<ModulesConnectionCreateDTO> listModuleCreateDTO = new ArrayList<>();
            List<ModulesConnectionUpdateDTO> listModuleUpdateDTO = new ArrayList<>();

            ApplicationCreateDTO applicationCreateDTO = new ApplicationCreateDTO();

            applicationCreateDTO.setCourseLeipzig(cL.getName());

            int rIdx = rand.nextInt(3) + 1;
            for (int i = 0; i < rIdx; i++) {
                ModulesConnectionCreateDTO modulesConnection = createModulesConnectionDTO(cL, moduleSettingsNode);
                listModuleCreateDTO.add(modulesConnection);
                ModulesConnectionUpdateDTO mUdto = new ModulesConnectionUpdateDTO();
                listModuleUpdateDTO.add(mUdto);
            }

            applicationCreateDTO.setModulesConnections(listModuleCreateDTO);

            String vorgangsnummer = applicationService.createApplication(applicationCreateDTO);
            

            // updating the status of those randomGen Applications:
            ApplicationUpdateDTO applicationUpdateDTO = new ApplicationUpdateDTO();
            ModulesConnectionUpdateDTO modulesConnectionDTO;
            Application application = applicationService.getApplicationById(vorgangsnummer);


            if (closed > 0) { // update application to ABGESCHLOSSEN
                List<ModulesConnection> connectionsCopy = new ArrayList<>(application.getModulesConnections());
                for (ModulesConnection mc : connectionsCopy) {
                    applicationUpdateDTO.setUserRole("pav");
                    modulesConnectionDTO = updateModulesConnectionDTO(mc.getId(), ABGESCHLOSSEN);
                    applicationUpdateDTO.setModulesConnections(List.of(modulesConnectionDTO));
                    applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO);
                }


                System.out.println("Created Dummy Application: " + vorgangsnummer + " as ABGESCHLOSSEN");
                closed--;
                continue;
            }
            if (pav > 0) { // update application to PRUEFUNGSAUSSCHUSS
                List<ModulesConnection> connectionsCopy = new ArrayList<>(application.getModulesConnections());
                for (ModulesConnection mc : connectionsCopy) {
                    applicationUpdateDTO.setUserRole("study-office");
                    modulesConnectionDTO = updateModulesConnectionDTO(mc.getId(), PRÜFUNGSAUSSCHUSS);
                    applicationUpdateDTO.setModulesConnections(List.of(modulesConnectionDTO));
                    applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO);
                }

                System.out.println("Updated Application: " + vorgangsnummer + " to PRUEFUNGSAUSSCHUSS");
                pav--;
                continue;
            }

            if (studyOffice > 0) { // update application to STUDIENBUERO
                List<ModulesConnection> connectionsCopy = new ArrayList<>(application.getModulesConnections());
                for (ModulesConnection mc : connectionsCopy) {
                    applicationUpdateDTO.setUserRole("study-office");
                    modulesConnectionDTO = updateModulesConnectionDTO(mc.getId(), STUDIENBÜRO);
                    applicationUpdateDTO.setModulesConnections(List.of(modulesConnectionDTO));
                    applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO);
                }

                System.out.println("Updated Application: " + vorgangsnummer + " to STUDIENBUERO");
                studyOffice--;
                continue;
            }
            if (open > 0) { // update application to ABGESCHLOSSEN
                System.out.println("Created Dummy Application: " + vorgangsnummer + "as NEW");
                open--;
                continue;
            }
        }
        System.out.println("Dataloader: Testdata successfully loaded into Database"); 
    }



    //Json Helper methods:

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



    //Helper methods for creating and updating application data in the database:

    /**
     * The function `createModuleDTO` creates a `ModuleBlockCreateDTO` object with various properties and
     * sets a description file based on a resource file.
     * 
     * @param cL An instance of the CourseLeipzig class, which contains information about the Leipzig
     * course.
     * @param moduleSettingNode A JSON object containing the settings for a module. It has the following
     * properties: name, university, points, pointsystem, comment.
     * @return The method is returning a ModuleBlockCreateDTO object.
     */


    private ModulesConnectionCreateDTO createModulesConnectionDTO(CourseLeipzig cL, JsonNode moduleSettingNode) {
        ModulesConnectionCreateDTO modulesConnectionCreateDTO = new ModulesConnectionCreateDTO();
        Random rdm = new Random();

        modulesConnectionCreateDTO.setCommentApplicant(getRandValueOfNode(moduleSettingNode, "comment", rdm));

        List<ModuleApplicationCreateDTO> moduleApplications = new ArrayList<>();
        int numberOfModuleApplications = rdm.nextInt(3)+1;
        for (int i = 0; i < numberOfModuleApplications; i++) {
            moduleApplications.add(createModuleApplicationDTO(moduleSettingNode));
        }
        modulesConnectionCreateDTO.setModuleApplications(moduleApplications);

        List<String> listModuleLeipzig = new ArrayList<>();
        int numberOfModulesLeipzig = rdm.nextInt(3)+1;
        for (int i = 0; i < numberOfModulesLeipzig; i++) {
            int idxR = rdm.nextInt(cL.getModulesLeipzigCourse().size());
            listModuleLeipzig.add(cL.getModulesLeipzigCourse().get(idxR).getName());
        }
        modulesConnectionCreateDTO.setModulesLeipzig(listModuleLeipzig);

        return modulesConnectionCreateDTO;
    }

    private ModuleApplicationCreateDTO createModuleApplicationDTO(JsonNode moduleSettingNode) {
        ModuleApplicationCreateDTO moduleApplicationCreateDTO = new ModuleApplicationCreateDTO();

        Random rdm = new Random();
        moduleApplicationCreateDTO.setName(moduleSettingNode.get("name").asText());
        moduleApplicationCreateDTO.setUniversity(moduleSettingNode.get("uni").asText());
        moduleApplicationCreateDTO.setPoints(Integer.parseInt(getRandValueOfNode(moduleSettingNode, "points", rdm)));
        moduleApplicationCreateDTO.setPointSystem(getRandValueOfNode(moduleSettingNode, "pointSystem", rdm));

        MultipartFile pdfMultipartFile = new MockMultipartFile("dummy", "dummy.pdf", "application/pdf", "pdf_data_mock".getBytes());
        moduleApplicationCreateDTO.setDescription(pdfMultipartFile);

        return moduleApplicationCreateDTO;
    }

    /**
     * The function retrieves a String from a random index from a specified node in a JSON object.
     * 
     * @param currentNode The `currentNode` parameter is a `JsonNode` object that represents the current
     * node in a JSON structure.
     * @param nodeName The `nodeName` parameter is a `String` that represents the name of the node one layer
     * below the given JsonNode in the JSON structure that you want to retrieve a random value from.
     * @param rdm The "rdm" parameter is an instance of the Random class, which is used to generate random
     * numbers. It is passed as an argument to the method so that the method can use it to generate a
     * random index for selecting a value from the "valueNode".
     * @return The method is returning a randomly selected value from a specific node in a JSON object.
     */
    private String getRandValueOfNode(JsonNode currentNode, String nodeName, Random rdm) {
        JsonNode valueNode = currentNode.get(nodeName);
        int rdmIdx = rdm.nextInt(valueNode.size());
        return valueNode.get(rdmIdx).asText();
    }

    private ModulesConnectionUpdateDTO updateModulesConnectionDTO(Long modulesConnectionId, EnumApplicationStatus status) {
        ModulesConnectionUpdateDTO modulesConnection = new ModulesConnectionUpdateDTO();
        modulesConnection.setId(modulesConnectionId);

        if(status == STUDIENBÜRO) {
            modulesConnection.setDecisionSuggestion(generateDecision(50));
            modulesConnection.setCommentStudyOffice("comment study office");
        }

        if(status == PRÜFUNGSAUSSCHUSS) {
            modulesConnection.setDecisionSuggestion(generateDecision(0));
            modulesConnection.setCommentStudyOffice("comment study office");

            modulesConnection.setDecisionFinal(generateDecision(70));
            modulesConnection.setCommentDecision("comment pav");
        }

        if(status == ABGESCHLOSSEN) {
            modulesConnection.setDecisionSuggestion(generateDecision(0));
            modulesConnection.setCommentStudyOffice("comment study office");

            modulesConnection.setDecisionFinal(generateDecision(0));
            modulesConnection.setCommentDecision("comment pav");
        }

        return modulesConnection;
    }

    public EnumModuleConnectionDecision generateDecision(double probabilityUnbearbeitet) {
        Random rand = new Random();
        double chooseUnbearbeitet = rand.nextDouble() * 100;

        if (chooseUnbearbeitet < probabilityUnbearbeitet) {
            return unedited;
        } else {
            int index = rand.nextInt(3);

            if (index == 0) return accepted;
            if(index == 1) return asExamCertificate;
            return denied;
        }
    }
}