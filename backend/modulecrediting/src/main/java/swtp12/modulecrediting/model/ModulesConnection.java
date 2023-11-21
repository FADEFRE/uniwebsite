package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
 public class ModulesConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String decision; //Enum maybe
    private String comment;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "module_application_id")
    private ModuleApplication moduleApplication;

    @ManyToMany
    @JoinTable(
            name = "module_connection_module_leipzig",
            joinColumns = @JoinColumn(name = "module_connection_id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id")
    )
    private List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();
    

    public ModulesConnection(String decision, String comment) {
        this.decision = decision;
        this.comment = comment;
    }

   public void setModuleApplication(ModuleApplication moduleApplication) {
        moduleApplication.setModulesConnection(this);
      this.moduleApplication = moduleApplication;
   }
}