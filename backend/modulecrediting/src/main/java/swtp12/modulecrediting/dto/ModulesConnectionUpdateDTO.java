package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.EnumModuleConnectionDecision;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModulesConnectionUpdateDTO {
    private Long id;
    private Boolean formalRejection;
    private String formalRejectionComment;
    private String commentStudyOffice;
    private EnumModuleConnectionDecision decisionSuggestion;
    private String commentDecision;
    private EnumModuleConnectionDecision decisionFinal;

    private List<ModuleApplicationUpdateDTO> moduleApplications;
    private List<ModuleLeipzigUpdateDTO> modulesLeipzig;

    @Override
    public String toString() {
        return "ModulesConnectionUpdateDTO{" +
                "id=" + id +
                ", commentStudyOffice='" + commentStudyOffice + '\'' +
                ", decisionSuggestion=" + decisionSuggestion +
                ", commentDecision='" + commentDecision + '\'' +
                ", decisionFinal=" + decisionFinal +
                ", moduleApplications=" + moduleApplications +
                ", modulesLeipzig=" + modulesLeipzig +
                '}';
    }
}
