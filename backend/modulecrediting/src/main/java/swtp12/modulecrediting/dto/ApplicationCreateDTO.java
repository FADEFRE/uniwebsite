package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.LifecycleState;


@Getter
@Setter
@NoArgsConstructor
public class ApplicationCreateDTO {
    private String courseLeipzig;
    private List<ModulesConnectionCreateDTO> modulesConnections;
}
