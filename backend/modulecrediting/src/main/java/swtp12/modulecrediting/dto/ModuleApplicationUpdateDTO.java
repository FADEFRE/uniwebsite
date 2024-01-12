package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModuleApplicationUpdateDTO {
    private Long id;
    private String name;
    private String university;
    private Integer points;
    private String pointSystem;
}
