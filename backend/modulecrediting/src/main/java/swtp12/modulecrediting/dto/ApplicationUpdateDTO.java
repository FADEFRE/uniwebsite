package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class ApplicationUpdateDTO {
    private String userRole; // "study_office" / "pav"
    private String courseLeipzig;
    private List<ModulesConnectionUpdateDTO> modulesConnections;
}
