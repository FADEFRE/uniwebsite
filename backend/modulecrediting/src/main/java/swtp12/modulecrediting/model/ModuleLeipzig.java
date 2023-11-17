package swtp12.modulecrediting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ModuleLeipzig {   
    @Id
    @GeneratedValue
    private Long id;
    private String moduleName;
    private String moduleCode;
    private String course;

    // @ManyToMany
    // private List<ModulesConnection> modulesConnection;


    public ModuleLeipzig(String moduleName, String moduleCode, String course) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.course = course;
    }
}
