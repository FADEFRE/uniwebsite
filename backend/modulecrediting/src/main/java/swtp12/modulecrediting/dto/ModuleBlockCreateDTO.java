package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ModuleBlockCreateDTO {
    private String moduleName;
    private String university;
    private Integer points;
    private String pointSystem;
    private MultipartFile description;
    private String moduleNameLeipzig;
    private String commentApplicant;
}