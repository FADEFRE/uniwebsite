package swtp12.modulecrediting.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.ExternalModule;

/**
 * DTO for creating and updating an {@link ExternalModule}
 */
@Getter
@Setter
@NoArgsConstructor
public class ExternalModuleDTO {
    private Long id;
    private String name;
    private String externalCourse;
    private String university;
    private String points;
    private String pointSystem;
    private MultipartFile description;
}