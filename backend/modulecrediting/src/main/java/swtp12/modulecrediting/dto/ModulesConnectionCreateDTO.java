package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModulesConnectionCreateDTO {
    private String commentApplicant;
    private List<ExternalModuleCreateDTO> externalModules;
    private List<String> modulesLeipzig;
}
