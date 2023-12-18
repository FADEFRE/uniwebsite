package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;



@Getter
@Setter
@NoArgsConstructor
public class ModuleBlockCreateDTO {
    private String moduleName;
    private String university;
    private Integer points;
    private String pointSystem;
    private MultipartFile description;
    private List<String> moduleNamesLeipzig;
    private String commentApplicant;
}