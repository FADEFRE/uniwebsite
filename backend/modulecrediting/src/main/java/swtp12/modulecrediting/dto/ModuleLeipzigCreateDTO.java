package swtp12.modulecrediting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModuleLeipzigCreateDTO {
    private String moduleName;
    
    @JsonIgnore
    private String moduleCode;
}
