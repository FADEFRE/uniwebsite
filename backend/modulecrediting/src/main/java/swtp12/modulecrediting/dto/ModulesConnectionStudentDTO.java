package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.ModuleApplication;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.model.ModulesConnection;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class ModulesConnectionStudentDTO {
    private Long id;
    private ModulesConnection.ModuleConnectionDecision decision;
    private ModuleApplication moduleApplication;
    private List<ModuleLeipzig> modulesLeipzig;

    public ModulesConnectionStudentDTO(Long id, ModulesConnection.ModuleConnectionDecision decision, ModuleApplication moduleApplication, List<ModuleLeipzig> modulesLeipzig) {
        this.id = id;
        this.decision = decision;
        this.moduleApplication = moduleApplication;
        this.modulesLeipzig = modulesLeipzig;
    }
}
