package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.Application;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationStudentDTO {
    private Long id;
    private Application.ApplicationStatus fullStatus;
    private CourseLeipzigWithoutModulesDTO courseLeipzig;
    private List<ModulesConnectionStudentDTO> modulesConnections;

    public ApplicationStudentDTO(Long id, Application.ApplicationStatus fullStatus, CourseLeipzigWithoutModulesDTO courseLeipzig, List<ModulesConnectionStudentDTO> modulesConnections) {
        this.id = id;
        this.fullStatus = fullStatus;
        this.courseLeipzig = courseLeipzig;
        this.modulesConnections = modulesConnections;
    }
}
