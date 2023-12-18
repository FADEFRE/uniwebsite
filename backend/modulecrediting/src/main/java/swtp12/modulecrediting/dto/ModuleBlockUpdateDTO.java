package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import swtp12.modulecrediting.model.EnumModuleConnectionDecision;


@Getter
@Setter
@NoArgsConstructor
public class ModuleBlockUpdateDTO {
    private String moduleName;
    private String university;
    private Integer points;
    private String pointSystem;
    private List<String> moduleNamesLeipzig;
    private EnumModuleConnectionDecision decisionFinal;
    private EnumModuleConnectionDecision decisionSuggestion;
    private String commentStudyOffice;
    private String commentDecision;
    private Boolean asExamCertificate;
}
