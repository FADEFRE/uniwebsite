package swtp12.modulecrediting.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import lombok.NoArgsConstructor;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.Views;

@Data
@NoArgsConstructor
@JsonView(Views.ApplicationStudent.class)
public class StudentApplicationDTO {
    private String id;
    private String fullStatus;

    private LocalDateTime creationDate;
    private LocalDateTime lastEditedDate;
    private LocalDateTime decisionDate;

    private CourseLeipzig courseLeipzig;

    private List<StudentModulesConnectionDTO> modulesConnections;

}
