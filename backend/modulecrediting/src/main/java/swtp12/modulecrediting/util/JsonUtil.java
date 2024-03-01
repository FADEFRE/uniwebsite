package swtp12.modulecrediting.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode grabJsonNodeFromMultipartFile(MultipartFile multipartFile, String nodeName) {
        JsonNode jsonNode;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            jsonNode = objectMapper.readValue(inputStream, JsonNode.class);
        } 
        catch (IOException e) { throw new RuntimeException("Failed to read JSON data", e); }

        return Optional.ofNullable(jsonNode)
                .map(j -> j.get(nodeName))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }

    public static JsonNode grabJsonNodeFromJsonNode(JsonNode currentNode, String nodeName) {
        return Optional.ofNullable(currentNode)
        .map(j -> j.get(nodeName))
        .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }
    
}
