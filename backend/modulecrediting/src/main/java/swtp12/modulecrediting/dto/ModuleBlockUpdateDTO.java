package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import swtp12.modulecrediting.model.ModuleConnectionDecision;



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
    private ModuleConnectionDecision decisionSuggestion;
    private String commentStudyOffice;
    private String commentDecision;
}
