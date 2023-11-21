package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany(mappedBy = "modulesLeipzig")
    private List<ModulesConnection> modulesConnections = new ArrayList<>();


    public ModuleLeipzig(String moduleName, String moduleCode, String course) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.course = course;
    }
}
