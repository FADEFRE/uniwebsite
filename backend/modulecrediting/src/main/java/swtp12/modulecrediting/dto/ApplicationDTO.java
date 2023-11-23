package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.ModulesConnection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// for api endpoint /applications/{id}
public class ApplicationDTO {
    private Long id;
    private Application.ApplicationStatus fullStatus;
    private LocalDate creationDate;
    private LocalDate decisionDate;
    private CourseLeipzigWithoutModulesDTO courseLeipzig;
    private List<ModulesConnection> modulesConnections = new ArrayList<>();

    public ApplicationDTO(Long id, Application.ApplicationStatus fullStatus, LocalDate creationDate, LocalDate decisionDate, CourseLeipzigWithoutModulesDTO courseLeipzig, List<ModulesConnection> modulesConnections) {
        this.id = id;
        this.fullStatus = fullStatus;
        this.creationDate = creationDate;
        this.decisionDate = decisionDate;
        this.courseLeipzig = courseLeipzig;
        this.modulesConnections = modulesConnections;
    }

}
