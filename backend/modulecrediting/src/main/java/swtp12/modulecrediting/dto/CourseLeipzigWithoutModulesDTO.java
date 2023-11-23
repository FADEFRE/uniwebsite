package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseLeipzigWithoutModulesDTO {
    private Long id;
    private String name;

    public CourseLeipzigWithoutModulesDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
