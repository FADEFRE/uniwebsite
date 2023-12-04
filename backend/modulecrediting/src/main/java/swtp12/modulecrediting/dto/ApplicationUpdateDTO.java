package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class ApplicationUpdateDTO {
    private String userRole; // "study_office" / "pav"
    private List<ModuleBlockUpdateDTO> moduleBlockUpdateDTOList;
}
