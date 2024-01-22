package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;
import swtp12.modulecrediting.model.ExternalModule;
import swtp12.modulecrediting.model.ModuleLeipzig;

@Data
@NoArgsConstructor
public class StudentModulesConnectionDTO {
    private Long id;

    private EnumModuleConnectionDecision decisionFinal;
    private String commentDecision;

    private Boolean formalRejection;
    private String formalRejectionComment;

    private String commentApplicant;

    private List<ExternalModule> externalModules;
    private List<ModuleLeipzig> modulesLeipzig;

}
