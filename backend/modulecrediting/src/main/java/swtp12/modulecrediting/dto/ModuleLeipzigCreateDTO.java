package swtp12.modulecrediting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// used to create a new module leipzig !! different to all the other create & update dtos. they are used for creating,updating an application
public class ModuleLeipzigCreateDTO {
    private String moduleName;

    @JsonIgnore
    private String moduleCode;
}
