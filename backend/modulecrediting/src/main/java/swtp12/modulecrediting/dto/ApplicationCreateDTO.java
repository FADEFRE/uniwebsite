package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApplicationCreateDTO {
    private String courseLeipzig;
    private List<ModuleBlockCreateDTO> moduleBlockCreateDTOList;
}
