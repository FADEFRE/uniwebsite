package swtp12.modulecrediting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swtp12.modulecrediting.model.ModuleLeipzig;

/**
 * DTO for creating and updating {@link ModuleLeipzig}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleLeipzigDTO {
    private String name;
    private String code;
}
