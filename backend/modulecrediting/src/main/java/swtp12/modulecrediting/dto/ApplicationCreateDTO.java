package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationCreateDTO {
    private String courseLeipzig;
    private List<ModuleBlockCreateDTO> moduleBlockCreateDTOList;
}
