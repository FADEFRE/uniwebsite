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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;


import swtp12.modulecrediting.dto.ApplicationCreateDTO;
import swtp12.modulecrediting.dto.ApplicationUpdateDTO;
import swtp12.modulecrediting.dto.ExternalModuleCreateDTO;
import swtp12.modulecrediting.dto.ExternalModuleUpdateDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigUpdateDTO;
import swtp12.modulecrediting.dto.ModulesConnectionCreateDTO;
import swtp12.modulecrediting.dto.ModulesConnectionUpdateDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.RoleRepository;
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
    private ApplicationService applicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder encoder;

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

        roleCreation();

        userCreation(testData);

        leipzigDataLoader(moduleLeipzigData);

        createTestData(testData);
    }


    private void roleCreation() {
        System.out.println("Creating Roles:");
        Role admin = new Role("ROLE_ADMIN");
        Role sb = new Role("ROLE_STUDY");
        Role pav = new Role("ROLE_CHAIR");
        roleRepository.save(admin);
        roleRepository.save(sb);
        roleRepository.save(pav);
    }



    //creates users defined in filename (test data)
    private void userCreation(String fileName) {
        System.out.println("Creating Users:");
        JsonNode userSettings = grabFirstNodeFromJson(fileName, "users");
        for (JsonNode user : userSettings) {
            String username = user.get("name").asText();
            String password = user.get("password").asText();
            String roleName = user.get("role").asText();
            
            Optional<User> userCandidate = userRepository.findByUsername(username);

            if (!userCandidate.isPresent()) {
                User userCreate = new User(
                    username,
                    encoder.encode(password),
                    true
                );
                Optional<Role> roleCandidate = roleRepository.findByRoleName(roleName);
                if (roleCandidate.isPresent()) {
                    if(userCreate.getRole() == null) {
                        userCreate.setRole(roleCandidate.get());
                    }
                    userRepository.save(userCreate);
                }
            }
        }
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
            String courseName = courseNode.get("name").asText();
            System.out.println("Processing course: " + courseName);
            CourseLeipzig courseLeipzig = courseLeipzigRepo.findByName(courseName)
                    .orElseGet(() -> {
                        System.out.println("Creating new course: " + courseName);
                        return courseLeipzigRepo.save(new CourseLeipzig(courseName));
                    });

            JsonNode modules = grabModulesFromJsonNode(courseNode);
            for (JsonNode module : modules) {
                String moduleName = module.get("name").asText();
                String moduleCode = module.get("number").asText();
                System.out.println("Processing module: " + moduleName + " with code: " + moduleCode);
                ModuleLeipzig moduleLeipzig = modulLeipzigRepo.findByName(moduleName)
                        .orElseGet(() -> {
                            System.out.println("Creating new module: " + moduleName + " with code: " + moduleCode);
                            return modulLeipzigRepo.save(new ModuleLeipzig(moduleName, moduleCode));
                        });

                courseLeipzig.addModulesLeipzig(moduleLeipzig);
                courseLeipzigRepo.save(courseLeipzig);
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
        JsonNode moduleSettingsNode = grabFirstNodeFromJson(testFileName, "randExternalModules").get(0);
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

            ApplicationCreateDTO applicationCreateDTO = new ApplicationCreateDTO();

            applicationCreateDTO.setCourseLeipzig(cL.getName());

            int rIdx = rand.nextInt(3) + 2;
            for (int i = 0; i < rIdx; i++) {
                ModulesConnectionCreateDTO modulesConnection = createModulesConnectionDTO(cL, moduleSettingsNode);
                listModuleCreateDTO.add(modulesConnection);
            }

            applicationCreateDTO.setModulesConnections(listModuleCreateDTO);

            String vorgangsnummer = applicationService.createApplication(applicationCreateDTO);
            

            // updating the status of those randomGen Applications:
            ApplicationUpdateDTO applicationUpdateDTO = new ApplicationUpdateDTO();
            Application application = applicationService.getApplicationById(vorgangsnummer);

            String updatedData = "";
            if (closed > 0) { // update application to ABGESCHLOSSEN
                List<ModulesConnectionUpdateDTO> mcuDTO = new ArrayList<>();
                mcuDTO = updateModulesConnectionDTO(ABGESCHLOSSEN, application);
                applicationUpdateDTO.setModulesConnections(mcuDTO);

                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "study-office");
                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "chairman");
                applicationService.updateApplicationStatus(vorgangsnummer);
                updatedData = "closed";
                closed--;
            }
            else if (pav > 0) { // update application to PRUEFUNGSAUSSCHUSS
                List<ModulesConnectionUpdateDTO> mcuDTO = new ArrayList<>();
                mcuDTO = updateModulesConnectionDTO(PRÜFUNGSAUSSCHUSS, application);
                applicationUpdateDTO.setModulesConnections(mcuDTO);

                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "study-office");
                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "chairman");
                applicationService.updateApplicationStatus(vorgangsnummer);

                updatedData = "pav";
                pav--;
            }
            else if (studyOffice > 0) { // update application to STUDIENBUERO
                List<ModulesConnectionUpdateDTO> mcuDTO = new ArrayList<>();
                mcuDTO = updateModulesConnectionDTO(STUDIENBÜRO, application);
                applicationUpdateDTO.setModulesConnections(mcuDTO);
                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "study-office");
                applicationService.updateApplicationStatus(vorgangsnummer);
                updatedData = "studyOffice";
                studyOffice--;
            }
            else if (open > 0) { // update application to ABGESCHLOSSEN
                updatedData = "open";
                open--;
            }
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
    private ModulesConnectionCreateDTO createModulesConnectionDTO(CourseLeipzig cL, JsonNode moduleSettingNode) {
        ModulesConnectionCreateDTO modulesConnectionCreateDTO = new ModulesConnectionCreateDTO();
        Random rdm = new Random();

        modulesConnectionCreateDTO.setCommentApplicant(getRandValueOfNode(moduleSettingNode, "comment", rdm));

        List<ExternalModuleCreateDTO> externalModulesCreateDTOs = new ArrayList<>();
        int numberofExternalModules = rdm.nextInt(3)+1;
        for (int i = 0; i < numberofExternalModules; i++) {
            externalModulesCreateDTOs.add(createExternalModuleDTO(moduleSettingNode));
        }
        modulesConnectionCreateDTO.setExternalModules(externalModulesCreateDTOs);

        List<String> listModuleLeipzig = new ArrayList<>();
        int numberOfModulesLeipzig = rdm.nextInt(3)+1;
        for (int i = 0; i < numberOfModulesLeipzig; i++) {
            int idxR = rdm.nextInt(cL.getModulesLeipzigCourse().size());
            listModuleLeipzig.add(cL.getModulesLeipzigCourse().get(idxR).getName());
        }
        modulesConnectionCreateDTO.setModulesLeipzig(listModuleLeipzig);

        return modulesConnectionCreateDTO;
    }

    private ExternalModuleCreateDTO createExternalModuleDTO(JsonNode moduleSettingNode) {
        ExternalModuleCreateDTO externalModuleCreateDTO = new ExternalModuleCreateDTO();

        Random rdm = new Random();
        externalModuleCreateDTO.setName(getRandValueOfNode(moduleSettingNode, "name", rdm));
        externalModuleCreateDTO.setUniversity(getRandValueOfNode(moduleSettingNode, "uni", rdm));
        externalModuleCreateDTO.setPoints(Integer.parseInt(getRandValueOfNode(moduleSettingNode, "points", rdm)));
        externalModuleCreateDTO.setPointSystem(getRandValueOfNode(moduleSettingNode, "pointSystem", rdm));

        MultipartFile pdfMultipartFile = new MockMultipartFile("dummy", "dummy.pdf", "application/pdf", "pdf_data_mock".getBytes());
        externalModuleCreateDTO.setDescription(pdfMultipartFile);

        return externalModuleCreateDTO;
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

    private List<ModulesConnectionUpdateDTO> updateModulesConnectionDTO(EnumApplicationStatus status, Application application) {
        
        List<ModulesConnectionUpdateDTO> mcuDTO = new ArrayList<>();
        boolean studyBool = true;
        boolean pavBool = true;
        for (ModulesConnection modCon : application.getModulesConnections()) { 
            //Modul listen
            ModulesConnectionUpdateDTO modulesConnectionDTO = new ModulesConnectionUpdateDTO();

            modulesConnectionDTO.setId(modCon.getId());
            
            for (ModuleLeipzig mL : modCon.getModulesLeipzig()) {
                ModuleLeipzigUpdateDTO mluDTO = new ModuleLeipzigUpdateDTO();
                mluDTO.setName(mL.getName());
                if (modulesConnectionDTO.getModulesLeipzig() == null) {
                    List<ModuleLeipzigUpdateDTO> mluDTOs = new ArrayList<>();
                    mluDTOs.add(mluDTO);
                    modulesConnectionDTO.setModulesLeipzig(mluDTOs);
                }
                else modulesConnectionDTO.getModulesLeipzig().add(mluDTO);
            }

            for (ExternalModule eM : modCon.getExternalModules()) {
                ExternalModuleUpdateDTO mauDTO = new ExternalModuleUpdateDTO();
                mauDTO.setId(eM.getId());
                mauDTO.setName(eM.getName());
                mauDTO.setUniversity(eM.getUniversity());
                mauDTO.setPoints(eM.getPoints());
                mauDTO.setPointSystem(eM.getPointSystem());
                if (modulesConnectionDTO.getExternalModules() == null) {
                    List<ExternalModuleUpdateDTO> emuDTOs = new ArrayList<>();
                    emuDTOs.add(mauDTO);
                    modulesConnectionDTO.setExternalModules(emuDTOs);
                }
                else modulesConnectionDTO.getExternalModules().add(mauDTO);
            }
            //comments and decisions
            if(status == STUDIENBÜRO) {
                if (studyBool) { 
                    modulesConnectionDTO.setDecisionSuggestion(unedited); 
                    studyBool = false;
                }
                else { modulesConnectionDTO.setDecisionSuggestion(generateDecision()); }
                
                modulesConnectionDTO.setCommentStudyOffice("liegt beim study office");
            }

            if(status == PRÜFUNGSAUSSCHUSS) {
                modulesConnectionDTO.setDecisionSuggestion(generateDecision());
                modulesConnectionDTO.setCommentStudyOffice("comment study office");

                if (pavBool) { 
                    modulesConnectionDTO.setDecisionFinal(unedited); 
                    pavBool = false;
                }
                else { modulesConnectionDTO.setDecisionFinal(generateDecision()); }
                
                modulesConnectionDTO.setCommentDecision("liegt beim pav");
            }

            if(status == ABGESCHLOSSEN) {
                modulesConnectionDTO.setDecisionSuggestion(generateDecision());
                modulesConnectionDTO.setCommentStudyOffice("comment study office");

                modulesConnectionDTO.setDecisionFinal(generateDecision());
                modulesConnectionDTO.setCommentDecision("comment pav");
            }
            mcuDTO.add(modulesConnectionDTO);

        }
        return mcuDTO;
    }


    public EnumModuleConnectionDecision generateDecision() {
        Random rand = new Random();
        int index = rand.nextInt(3);
        if (index == 0) return accepted;
        if(index == 1) return asExamCertificate;
        return denied;
    }
}