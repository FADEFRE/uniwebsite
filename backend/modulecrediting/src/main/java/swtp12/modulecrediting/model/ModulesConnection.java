package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
 public class ModulesConnection {
    @Id
    @GeneratedValue
    private Long id;
    @JsonView(Views.ApplicationStudent.class)
    private ModuleConnectionDecision decision;
    @JsonView(Views.ApplicationLogin.class)
    private ModuleConnectionDecision decisionSuggestion;
    @JsonView(Views.ApplicationStudent.class)
    private String comment;

    //Relation ModulesConnection <-> ModuleApplication (Setter in ModuleApplication)
    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    @JsonView(Views.ApplicationStudent.class)
    private ModuleApplication moduleApplication;

    //Relation ModulesConnection <-> ModuleLeipzig
    @ManyToMany
    @JoinTable(
            name = "module_connection_module_leipzig",
            joinColumns = @JoinColumn(name = "module_connection_id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id")
    )
    @JsonManagedReference
    @JsonView(Views.ApplicationStudent.class)
    private List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();




    //Relation ModulesConnection <-> Application (Setter in Application)
    @ManyToOne
    @JsonBackReference
    private Application application;



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


    //Function to add ModuleApplication to this ModuleConnection (and add this ModuleConnection to the ModuleApplication)
    public void addModuleApplication(ModuleApplication moduleApplication) {
      moduleApplication.setModulesConnection(this);
      this.moduleApplication = moduleApplication;
    }

    //Function to add List of ModuleLeipzig to this ModulesConnection (and add this ModuleConnectio to all ModulesLeipzig in the List)
    public void addModulesLeipzig(List<ModuleLeipzig> modulesLeipzig) {
        for(ModuleLeipzig m : modulesLeipzig) { 
            m.getModulesConnections().add(this);
        }
       this.modulesLeipzig = modulesLeipzig;
   }
}