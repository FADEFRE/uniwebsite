package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;



@Getter
@Setter
@NoArgsConstructor
public class ExternalModuleCreateDTO {
    private String name;
    private String university;
    private Integer points;
    private String pointSystem;
    private MultipartFile description;
}