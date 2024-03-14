package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for getting or uploading {@code LeipzigDataCourse} via JSON
 */
@Getter
@Setter
@NoArgsConstructor
public class LeipzigDataCourseDTO {
    private String name;
    private List<ModuleLeipzigDTO> modules;
}
