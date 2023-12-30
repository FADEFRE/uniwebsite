package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModuleLeipzigUpdateDTO {
    private Boolean removeFromModulesConnection;
    private String moduleName;
}
