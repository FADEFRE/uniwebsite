package swtp12.modulecrediting.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This {@link Component} includes functions to grab a {@link JsonNode} from diffrent sources.
 */
@Component
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This function grabs a {@link JsonNode} with given {@link String name} from given {@link MultipartFile multipartFile}.
     * @param multipartFile {@code MultipartFile}
     * @param nodeName {@code String}
     * @return JsonNode
     * @see JsonNode
     * @see MultipartFile
     */
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

    /**
     * This function grabs a {@link JsonNode} with given {@link String name} from given {@link JsonNode currentNode}.
     * @param currentNode {@code JsonNode}
     * @param nodeName {@code String}
     * @return JsonNode
     * @see JsonNode
     */
    public static JsonNode grabJsonNodeFromJsonNode(JsonNode currentNode, String nodeName) {
        return Optional.ofNullable(currentNode)
        .map(j -> j.get(nodeName))
        .orElseThrow(() -> new IllegalArgumentException("Invalid JSON Object"));
    }
    
}
