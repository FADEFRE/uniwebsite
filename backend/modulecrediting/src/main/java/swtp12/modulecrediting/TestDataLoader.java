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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import swtp12.modulecrediting.dto.ApplicationDTO;
import swtp12.modulecrediting.dto.ExternalModuleDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.dto.ModulesConnectionDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.EnumApplicationStatus;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;
import swtp12.modulecrediting.repository.RoleRepository;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.service.ApplicationService;
import swtp12.modulecrediting.util.JsonUtil;
import swtp12.modulecrediting.util.LogUtil;


/**
 * The DataLoader class is responsible for reading JSON data and saving the data correctly into the database.
 */
@Component
public class TestDataLoader {
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


    public TestDataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * The run function reads Uni Leipzig Data from a JSON and writes it into the database.
     * Reads test data from a Json and writes it into the databse.
     * <p> It creates {@link User Users} defined in the JSON aswell.
     * 
     * <p> NOTE: This should only be used for testing and it is recommended to use it with "create-drop" for your database.
     * 
     * @see Application
     * @see CourseLeipzig
     * @see ModuleLeipzig
     * @see User
     */
    @Transactional
    public void run() {
        String testData = "/test_data.json";

        userCreation(testData);

        leipzigDataLoader(testData);

        createTestData(testData);
    }



    /**
     * Loads data from a JSON file into the database, creating and linking {@link CourseLeipzig} and {@link ModuleLeipzig} objects.
     * 
     * @param fileName The `fileName` parameter is the name of the JSON file that contains the data to be loaded into the database.
     * @see CourseLeipzig
     * @see ModuleLeipzig
     */
    private void userCreation(String fileName) {
        JsonNode userSettings = grabFirstNodeFromJson(fileName, "users");
        for (JsonNode user : userSettings) {
            String username = user.get("name").asText();
            String password = user.get("password").asText();
            String roleName = user.get("role").asText();
            LogUtil.printLog("Creating user: " + username);
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
        LogUtil.printLog("");
    }


    /**
     * Loads data from a JSON file into the database, creating and linking {@link CourseLeipzig} and {@link ModuleLeipzig} objects.
     * 
     * @param fileName The `fileName` parameter is the name of the JSON file that contains the data to be loaded into the database.
     * @see CourseLeipzig
     * @see ModuleLeipzig
     */
    @Transactional
    private void leipzigDataLoader(String fileName) {
        JsonNode courseNodes = grabFirstNodeFromJson(fileName, "courses");
        for (JsonNode courseNode : courseNodes) {
            String courseName = courseNode.get("name").asText();
            CourseLeipzig courseLeipzig = courseLeipzigRepo.findByName(courseName)
                    .orElseGet(() -> {
                        return courseLeipzigRepo.save(new CourseLeipzig(courseName));
                    });

            JsonNode modules = JsonUtil.grabJsonNodeFromJsonNode(courseNode, "modules");
            for (JsonNode module : modules) {
                String moduleName = module.get("name").asText();
                String moduleCode = module.get("number").asText();
                ModuleLeipzig moduleLeipzig = modulLeipzigRepo.findByName(moduleName)
                        .orElseGet(() -> {
                            return modulLeipzigRepo.save(new ModuleLeipzig(moduleName, moduleCode));
                        });

                courseLeipzig.addModulesLeipzig(moduleLeipzig);
                courseLeipzigRepo.save(courseLeipzig);
            }
        }
        LogUtil.printLog("Leipzig-Data successfully loaded into Database");
    }



    /**
     * This function generates dummy data for testing purposes by creating random {@link Application Applications}.
     * 
     * @param testFileName The `testFileName` parameter is the name of the JSON file from which the test
     * data settings will be extracted.
     * @see Application
     */
    @Transactional
    private void createTestData(String testFileName) {

        JsonNode applicationSettingsNode = grabFirstNodeFromJson(testFileName, "randApplications").get(0);
        JsonNode moduleSettingsNode = grabFirstNodeFromJson(testFileName, "randExternalModules").get(0);
        Random rand = new Random();

        int open = applicationSettingsNode.get("new").asInt();
        int studyOffice = applicationSettingsNode.get("study-office").asInt();
        int formfehler = applicationSettingsNode.get("formfehler").asInt();
        int pav = applicationSettingsNode.get("pav").asInt();
        int closed = applicationSettingsNode.get("closed").asInt();
        int total = open + studyOffice + formfehler + pav + closed;
        
        List<CourseLeipzig> listOfCourseLeipzig = new ArrayList<>();
        List<CourseLeipzig> listOfCLinDB = courseLeipzigRepo.findAll();
        for (int i = 0; i < total; i++) {
            int randomIdx = rand.nextInt(listOfCLinDB.size());
            CourseLeipzig courseLeipzig = listOfCLinDB.get(randomIdx);
            listOfCourseLeipzig.add(courseLeipzig);
        }
        
        LogUtil.printLog("");
        LogUtil.printLog("Generating random Dummy Applications:");
        List<String> listOfApplicationNumbersGenerated = new ArrayList<>();
        for (CourseLeipzig cL : listOfCourseLeipzig) {
            List<ModulesConnectionDTO> listModuleCreateDTO = new ArrayList<>();

            ApplicationDTO applicationDTO = new ApplicationDTO();

            applicationDTO.setCourseLeipzig(cL.getName());

            int rIdx = rand.nextInt(20) + 2;
            for (int i = 0; i < rIdx; i++) {
                ModulesConnectionDTO modulesConnection = createModulesConnectionDTO(cL, moduleSettingsNode);
                listModuleCreateDTO.add(modulesConnection);
            }

            applicationDTO.setModulesConnections(listModuleCreateDTO);

            String vorgangsnummer = applicationService.createApplication(applicationDTO);
            

            // updating the status of those randomGen Applications:
            ApplicationDTO applicationUpdateDTO = new ApplicationDTO();
            Application application = applicationService.getApplicationById(vorgangsnummer);


            if (closed > 0) { // update application to ABGESCHLOSSEN
                List<ModulesConnectionDTO> mcuDTO = new ArrayList<>();
                mcuDTO = updateModulesConnectionDTO(ABGESCHLOSSEN, application);
                applicationUpdateDTO.setModulesConnections(mcuDTO);

                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "study-office");
                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "chairman");
                applicationService.updateApplicationStatus(vorgangsnummer);

                listOfApplicationNumbersGenerated.add(vorgangsnummer);
                closed--;
            }
            else if (pav > 0) { // update application to PRUEFUNGSAUSSCHUSS
                List<ModulesConnectionDTO> mcuDTO = new ArrayList<>();
                mcuDTO = updateModulesConnectionDTO(PRÜFUNGSAUSSCHUSS, application);
                applicationUpdateDTO.setModulesConnections(mcuDTO);

                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "study-office");
                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "chairman");
                applicationService.updateApplicationStatus(vorgangsnummer);

                listOfApplicationNumbersGenerated.add(vorgangsnummer);
                pav--;
            }
            else if (studyOffice > 0) { // update application to STUDIENBUERO
                List<ModulesConnectionDTO> mcuDTOs = new ArrayList<>();
                mcuDTOs = updateModulesConnectionDTO(STUDIENBÜRO, application);
                applicationUpdateDTO.setModulesConnections(mcuDTOs);
                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "study-office");
                applicationService.updateApplicationStatus(vorgangsnummer);

                listOfApplicationNumbersGenerated.add(vorgangsnummer);
                studyOffice--;
            }
            
            else if (formfehler > 0) { // update application to FORMFEHLER
                List<ModulesConnectionDTO> mcuDTOs = new ArrayList<>();
                mcuDTOs = updateModulesConnectionDTO(FORMFEHLER, application);
                applicationUpdateDTO.setModulesConnections(mcuDTOs);
                applicationService.updateApplication(vorgangsnummer, applicationUpdateDTO, "study-office");
                applicationService.updateApplicationStatus(vorgangsnummer);
                
                listOfApplicationNumbersGenerated.add(vorgangsnummer);
                formfehler--;
            }
            
            else if (open > 0) { // update application to ABGESCHLOSSEN
                listOfApplicationNumbersGenerated.add(vorgangsnummer);
                open--;
            }
        }

        LogUtil.printLog("");
        LogUtil.printLog("Generated Dummy Applications:");
        for (String string : listOfApplicationNumbersGenerated) {
            LogUtil.printLog("Application: " + string + " - " + applicationService.getApplicationById(string).getFullStatus());
        }
    }



    //Json Helper methods:

    /**
     * The function "grabFirstNodeFromJson" reads a JSON file from the given path and returns the first JsonNode
     * with the given name or throws mutliple exception if failed to do so.
     * 
     * <p> NOTE: This is an older documentation and might be out of date!
     * 
     * @exception IOException Gets thrown as RuntimeException if failed to read JSON data
     * @exception IllegalArgumentException Gets thrown if JsonNode is invalid
     * @param jsonPath String that represents the path to the JSON file that you want to read. 
     *                      It can be either an absolute path or a relative path to the JSON file.
     * @param nodeName String that represents the Name of the JsonNode you want to be returned
     * @return The JsonNode of the JSON file.
     * 
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

    //Helper methods for creating and updating application data in the database:

    /**
     * The function `createModuleDTO` creates a `ModuleBlockCreateDTO` object with various properties and
     * sets a description file based on a resource file.
     * 
     * <p> NOTE: This is an older documentation and might be out of date!
     * @param cL An instance of the CourseLeipzig class, which contains information about the Leipzig
     * course.
     * @param moduleSettingNode A JSON object containing the settings for a module. It has the following
     * properties: name, university, points, pointsystem, comment.
     * @return The method is returning a ModuleBlockCreateDTO object.
     */
    private ModulesConnectionDTO createModulesConnectionDTO(CourseLeipzig cL, JsonNode moduleSettingNode) {
        ModulesConnectionDTO modulesConnectionDTO = new ModulesConnectionDTO();
        Random rdm = new Random();

        modulesConnectionDTO.setCommentApplicant(getRandValueOfNode(moduleSettingNode, "comment", rdm));

        List<ExternalModuleDTO> externalModulesCreateDTOs = new ArrayList<>();
        int numberofExternalModules = rdm.nextInt(3)+1;
        for (int i = 0; i < numberofExternalModules; i++) {
            externalModulesCreateDTOs.add(createExternalModuleDTO(moduleSettingNode));
        }
        modulesConnectionDTO.setExternalModules(externalModulesCreateDTOs);

        List<ModuleLeipzigDTO> listModuleLeipzig = new ArrayList<>();
        int numberOfModulesLeipzig = rdm.nextInt(3)+1;
        for (int i = 0; i < numberOfModulesLeipzig; i++) {
            int idxR = rdm.nextInt(cL.getModulesLeipzigCourse().size());
            ModuleLeipzigDTO moduleLeipzigDTO = new ModuleLeipzigDTO();
            moduleLeipzigDTO.setName(cL.getModulesLeipzigCourse().get(idxR).getName());
            listModuleLeipzig.add(moduleLeipzigDTO);
        }
        modulesConnectionDTO.setModulesLeipzig(listModuleLeipzig);

        return modulesConnectionDTO;
    }

    /**
     * <p> NOTE: This is an older documentation and might be out of date!
     * @param moduleSettingNode
     * @return
     * 
     */
    private ExternalModuleDTO createExternalModuleDTO(JsonNode moduleSettingNode) {
        ExternalModuleDTO externalModuleDTO = new ExternalModuleDTO();

        Random rdm = new Random();
        externalModuleDTO.setName(getRandValueOfNode(moduleSettingNode, "name", rdm));
        externalModuleDTO.setExternalCourse(getRandValueOfNode(moduleSettingNode, "externalCourse", rdm));
        externalModuleDTO.setUniversity(getRandValueOfNode(moduleSettingNode, "uni", rdm));
        externalModuleDTO.setPoints(getRandValueOfNode(moduleSettingNode, "points", rdm));
        externalModuleDTO.setPointSystem(getRandValueOfNode(moduleSettingNode, "pointSystem", rdm));

        MultipartFile pdfMultipartFile = new MockMultipartFile("dummy", "dummy.pdf", "application/pdf", "pdf_data_mock".getBytes());
        externalModuleDTO.setDescription(pdfMultipartFile);

        return externalModuleDTO;
    }

    /**
     * The function retrieves a String from a random index from a specified node in a JSON object.
     * 
     * <p> NOTE: This is an older documentation and might be out of date!
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
     * <p> NOTE: This is an older documentation and might be out of date!
     * @param status
     * @param application
     * @return
     */
    private List<ModulesConnectionDTO> updateModulesConnectionDTO(EnumApplicationStatus status, Application application) {
        List<ModulesConnectionDTO> mcuDTO = new ArrayList<>();
        boolean studyBool = true;
        boolean pavBool = true;
        for (ModulesConnection modCon : application.getModulesConnections()) { 
            //Modul listen
            ModulesConnectionDTO modulesConnectionDTO = new ModulesConnectionDTO();

            modulesConnectionDTO.setId(modCon.getId());
            
            for (ModuleLeipzig mL : modCon.getModulesLeipzig()) {
                ModuleLeipzigDTO mluDTO = new ModuleLeipzigDTO();
                mluDTO.setName(mL.getName());
                if (modulesConnectionDTO.getModulesLeipzig() == null) {
                    List<ModuleLeipzigDTO> mluDTOs = new ArrayList<>();
                    mluDTOs.add(mluDTO);
                    modulesConnectionDTO.setModulesLeipzig(mluDTOs);
                }
                else modulesConnectionDTO.getModulesLeipzig().add(mluDTO);
            }

            for (ExternalModule eM : modCon.getExternalModules()) {
                ExternalModuleDTO mauDTO = new ExternalModuleDTO();
                mauDTO.setId(eM.getId());
                mauDTO.setName(eM.getName());
                mauDTO.setUniversity(eM.getUniversity());
                mauDTO.setPoints(eM.getPoints());
                mauDTO.setPointSystem(eM.getPointSystem());
                if (modulesConnectionDTO.getExternalModules() == null) {
                    List<ExternalModuleDTO> emuDTOs = new ArrayList<>();
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

            if(status == FORMFEHLER) {
                modulesConnectionDTO.setFormalRejection(true);
                modulesConnectionDTO.setFormalRejectionComment("Dickes F an Huy");
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

    /** 
     * 
     * <p> NOTE: This is an older documentation and might be out of date!
    */
    private EnumModuleConnectionDecision generateDecision() {
        Random rand = new Random();
        int index = rand.nextInt(3);
        if (index == 0) return accepted;
        if(index == 1) return asExamCertificate;
        return denied;
    }
}