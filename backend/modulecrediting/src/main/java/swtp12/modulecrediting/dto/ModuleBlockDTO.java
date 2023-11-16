package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ModuleBlockDTO {
    private String moduleName;
    private String course;
    private String university;
    private Integer creditPoints;
    private String creditSystem;
    private MultipartFile description;
    private String comment;
}
