package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CourseLeipzig {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany()
    @JoinColumn(name = "moduleLeipzig")
    private List<ModuleLeipzig> courseModuleLeipzigs = new ArrayList<ModuleLeipzig>();

    public CourseLeipzig(String name) {
        this.name = name;
    }

    
    public void setAllModuleLeipzigs(List<ModuleLeipzig> moduleLeipzigs) {
        for (ModuleLeipzig mL : moduleLeipzigs) {
            if (mL.getModuleName().equals(name)) {
                courseModuleLeipzigs.add(mL);
            }
        }
    }

    public void setModuleLeipzig(ModuleLeipzig moduleLeipzig) {
        courseModuleLeipzigs.add(moduleLeipzig);
    }
}
