package swtp12.modulecrediting;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import swtp12.modulecrediting.model.ModuleLeipzig;
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

    public DataLoader(ObjectMapper objectMapper, ModuleLeipzigRepository modulLeipzigRepo) {
        this.objectMapper = objectMapper;
        this.modulLeipzigRepo = modulLeipzigRepo;
    }

    @Override
    public void run(String... args) {
        List<ModuleLeipzig> modulsLeipzigs = new ArrayList<>();
        JsonNode json;

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/module_liste.json")) {
            json = objectMapper.readValue(inputStream, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data", e);
        }

        JsonNode courses = getCourses(json);
        for (JsonNode course : courses) {
            String courseName = course.get("name").asText();
            JsonNode modules = course.get("modules");
            for (JsonNode modul : modules) {
                modulsLeipzigs.add(createModulsFromNode(modul, courseName));
            }    
        }
        modulLeipzigRepo.saveAll(modulsLeipzigs);
    }

    private JsonNode getCourses(JsonNode json) {
        return Optional.ofNullable(json)
                .map(j -> j.get("courses"))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }

    private ModuleLeipzig createModulsFromNode(JsonNode modul, String courseName) {
        String name = modul.get("name").asText();
        String number = modul.get("number").asText();
        return new ModuleLeipzig(name, number, courseName);
    }

}