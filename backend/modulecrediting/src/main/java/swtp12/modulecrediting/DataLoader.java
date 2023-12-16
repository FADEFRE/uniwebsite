package swtp12.modulecrediting;

import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.ANGENOMMEN;
import static swtp12.modulecrediting.model.EnumUserRole.*;

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


import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.dto.ModuleBlockCreateDTO;
import swtp12.modulecrediting.dto.ModuleBlockUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.repository.UserRepository;
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
    private UserRepository userRepository;

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

        User study_office = new User("studyOff", "abc123", STUDY_OFFICE);
        User chairman = new User("chairM", "xyz789", CHAIRMAN);
        User admin = new User("admin", "admin", ADMIN);
        userRepository.save(study_office);
        userRepository.save(chairman);
        userRepository.save(admin);

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
        /*//Loads specified courses/modules 
        leipzigDataLoader(testFileName);
        System.out.print("\033[2K\033[1G");*/

        JsonNode applicationSettingsNode = grabFirstNodeFromJson(testFileName, "randApplications").get(0);
        JsonNode moduleSettingsNode = grabFirstNodeFromJson(testFileName, "randModuleApplications").get(0);
        Random rand = new Random();

        int open = applicationSettingsNode.get("offen").asInt();
        //int inEdit = applicationSettingsNode.get("bearbeitung").asInt();
        int onWait = applicationSettingsNode.get("warten").asInt();
        int closed = applicationSettingsNode.get("fertig").asInt();
        int total = open + onWait + closed;
        
        List<CourseLeipzig> listOfCourseLeipzig = new ArrayList<>();
        List<CourseLeipzig> listOfCLinDB = courseLeipzigRepo.findAll();
        for (int i = 0; i < total; i++) {
            int randomIdx = rand.nextInt(listOfCLinDB.size());
            CourseLeipzig courseLeipzig = listOfCLinDB.get(randomIdx);
            listOfCourseLeipzig.add(courseLeipzig);
        }
        
        System.out.println("Dataloader: Generating random Dummy Applications:");
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
            
            
            // updating the status of those randomGen Applications:
            ApplicationUpdateDTO applicationUpdateDTO = new ApplicationUpdateDTO();
            Application application = applicationService.getApplicationById(vorgangsnummer);

            String updatedData = "";
            int updater = onWait + closed;
            if (updater > 0) {
                if (closed > 0) {
                    //update as inEdit
                    updatedData = "onWait";
                    applicationUpdateDTO.setUserRole("study_office");
                    applicationUpdateDTO.setModuleBlockUpdateDTOList(updateModuleDTO("So", application));
                    closed--;
                } 
                else {
                    //update as closed
                    updatedData = "closed";
                    applicationUpdateDTO.setUserRole("pav");
                    applicationUpdateDTO.setModuleBlockUpdateDTOList(updateModuleDTO("Pav", application));
                    onWait--;
                }

                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO);
            }
            else if (open > 0) {
                updatedData = "open";
                open --;
            }



            


            //Printing out the randomGen Applications for ease of Use
            
            System.out.println("Created Dummy Application: " + vorgangsnummer + " as: " + updatedData);
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

        MultipartFile pdfMultipartFile = new MockMultipartFile("dummy", "dummy.pdf", "application/pdf", "pdf_data_mock".getBytes());
        moduleBlockCreateDTO.setDescription(pdfMultipartFile);

        return moduleBlockCreateDTO;
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

    /**
     * The `updateModuleDTO` function takes a user and an application as input and returns a list of
     * `ModuleBlockUpdateDTO` objects with updated information based on the user's role.
     * 
     * @param user A string representing the user's name.
     * @param application An Application object, which contains information about a specific
     * application.
     * @return The method `updateModuleDTO` returns a `List<ModuleBlockUpdateDTO>`.
     */
    private List<ModuleBlockUpdateDTO> updateModuleDTO(String user, Application application) {
        List<ModuleBlockUpdateDTO> moduleBlockUpdateDTOs = new ArrayList<>();
        
        for (ModulesConnection modCon : application.getModulesConnections()) {
            ModuleBlockUpdateDTO mBlockUpdateDTO = new ModuleBlockUpdateDTO();

            mBlockUpdateDTO.setModuleName(modCon.getModuleApplication().getName());
            mBlockUpdateDTO.setUniversity(modCon.getModuleApplication().getUniversity());
            mBlockUpdateDTO.setPoints(modCon.getModuleApplication().getPoints());
            mBlockUpdateDTO.setPointSystem(modCon.getModuleApplication().getPointSystem());
            mBlockUpdateDTO.setAsExamCertificate(modCon.getAsExamCertificate());
            mBlockUpdateDTO.setCommentDecision("finally inner peace");


            List<String> moduleNameList = new ArrayList<>();
            moduleNameList.add(modCon.getModulesLeipzig().get(0).getModuleName());
            //for (ModuleLeipzig mL : modCon.getModulesLeipzig()) { moduleNameList.add(mL.getModuleName()); }
            mBlockUpdateDTO.setModuleNamesLeipzig(moduleNameList);
            
            mBlockUpdateDTO.setDecisionSuggestion(ANGENOMMEN);
            if (user.equals("So")) {
                mBlockUpdateDTO.setCommentStudyOffice("Studyoffice Comment");
            } 
            else if (user.equals("Pav")){
                mBlockUpdateDTO.setCommentStudyOffice("PAV Comment");
                mBlockUpdateDTO.setDecisionFinal(ANGENOMMEN);
            }

            moduleBlockUpdateDTOs.add(mBlockUpdateDTO);
        }

        return moduleBlockUpdateDTOs;
    }
}