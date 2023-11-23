package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private ModuleConnectionDecision decision; //Enum maybe
    private ModuleConnectionDecision decisionSuggestion;
    private String comment;

    //Relation ModulesConnection <-> Application (Setter in Application)
    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonBackReference
    private Application application;

    //Relation ModulesConnection <-> ModuleApplication (Setter in ModuleApplication)
    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "module_application_id")
    @JsonManagedReference
    private ModuleApplication moduleApplication;

    //Relation ModulesConnection <-> ModuleLeipzig
    @ManyToMany
    @JoinTable(
            name = "module_connection_module_leipzig",
            joinColumns = @JoinColumn(name = "module_connection_id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id")
    )
    @JsonManagedReference
    private List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();


    public ModulesConnection(ModuleConnectionDecision decision, ModuleConnectionDecision decisionSuggestion, String comment) {
        this.decision = decision;
        this.decisionSuggestion = decisionSuggestion;
        this.comment = comment;
    }

    public enum ModuleConnectionDecision{
        ANGENOMMEN,
        VERAENDERT_ANGENOMMEN,
        ABGELEHNT,
        UNBEARBEITET
    }


    //Setter for Relation: ModulesConnection <-> ModuleApplication
    public void setModuleApplication(ModuleApplication moduleApplication) {
      moduleApplication.setModulesConnection(this);
      this.moduleApplication = moduleApplication;
    }

    // Setter for Relation: ModulesConnectiom <-> ModuleLeipzig
   public void setModulesLeipzig(ArrayList<ModuleLeipzig> modulesLeipzig) {
       for(ModuleLeipzig m : modulesLeipzig) m.addModulesConnection(this);
       this.modulesLeipzig = modulesLeipzig;
   }
}