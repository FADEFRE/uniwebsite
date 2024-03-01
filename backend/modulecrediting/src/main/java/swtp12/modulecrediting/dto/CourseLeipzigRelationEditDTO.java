package swtp12.modulecrediting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseLeipzigRelationEditDTO {
    private List<ModuleLeipzigDTO> modulesLeipzig;
}
