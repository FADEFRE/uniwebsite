package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModulesConnectionDTO {
    private Long id;

    private Boolean formalRejection;
    private String formalRejectionComment;

    private String commentStudyOffice;
    private EnumModuleConnectionDecision decisionSuggestion;

    private String commentDecision;
    private EnumModuleConnectionDecision decisionFinal;

    private String commentApplicant;

    private List<ExternalModuleDTO> externalModules;
    private List<ModuleLeipzigDTO> modulesLeipzig; //TODO: CHANGED DTO HERE CHECK IN POST REQUEST
}
