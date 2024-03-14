package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;

/**
 * DTO for updating the realtion between {@link CourseLeipzig} and {@link ModuleLeipzig}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseLeipzigRelationEditDTO {
    private List<ModuleLeipzigDTO> modulesLeipzig;
}
