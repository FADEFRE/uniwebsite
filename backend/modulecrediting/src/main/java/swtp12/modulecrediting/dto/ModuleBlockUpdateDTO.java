package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import swtp12.modulecrediting.model.ModuleConnectionDecision;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModuleBlockUpdateDTO {
    private String moduleName;
    private String university;
    private Integer points;
    private String pointSystem;
    private List<String> moduleNamesLeipzig;
    private ModuleConnectionDecision decision; // either suggestion or real depending on pav/studienbuero
    private String comment;
}
