package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.Setter;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ApplicationStudentDTO {
    private Long id;
    private Application.ApplicationStatus fullStatus;
    private CourseLeipzig courseLeipzig;
    private List<ModulesConnectionStudentDTO> modulesConnections;

    public ApplicationStudentDTO(Long id, Application.ApplicationStatus fullStatus, CourseLeipzig courseLeipzig, List<ModulesConnectionStudentDTO> modulesConnections) {
        this.id = id;
        this.fullStatus = fullStatus;
        this.courseLeipzig = courseLeipzig;
        this.modulesConnections = modulesConnections;
    }
}
