package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
public class ModulesConnection {
    @Id
    @GeneratedValue
    private Long id;
    @JsonView(Views.ApplicationStudent.class)
    @NotNull(message = "decisionFinal must not be null")
    private ModuleConnectionDecision decisionFinal;
    @JsonView(Views.ApplicationLogin.class)
    @NotNull(message = "decisionSuggestion must not be null")
    private ModuleConnectionDecision decisionSuggestion;
    @JsonView(Views.ApplicationStudent.class)
    private String commentDecision;
    @JsonView(Views.ApplicationStudent.class)
    private String commentStudyOffice;

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


    public ModulesConnection(ModuleConnectionDecision decisionFinal, ModuleConnectionDecision decisionSuggestion, String commentDecision, String commentStudyOffice) {
        this.decisionFinal = decisionFinal;
        this.decisionSuggestion = decisionSuggestion;
        this.commentDecision = commentDecision;
        this.commentStudyOffice = commentStudyOffice;
    }


    //Function to add ModuleApplication to this ModuleConnection (and add this ModuleConnection to the ModuleApplication)
    public void addModuleApplication(ModuleApplication moduleApplication) {
        moduleApplication.setModulesConnection(this);
        this.moduleApplication = moduleApplication;
    }

    //Function to set List of ModuleLeipzig to this ModulesConnection (and add this ModuleConnectio to all ModulesLeipzig in the List)
    public void setModulesLeipzig(List<ModuleLeipzig> modulesLeipzig) {
        for(ModuleLeipzig m : modulesLeipzig) { 
            m.getModulesConnections().add(this);
        }
    this.modulesLeipzig = modulesLeipzig;
    }

    public void addModulesLeipzig(List<ModuleLeipzig> modulesLeipzig) {
        for(ModuleLeipzig m : modulesLeipzig) {
            m.getModulesConnections().add(this);
            this.modulesLeipzig.add(m);
        }
    }

    public void removeModulesLeipzig(List<ModuleLeipzig> modulesLeipzig) {
        for(ModuleLeipzig m : modulesLeipzig) {
            m.getModulesConnections().remove(this);
            this.modulesLeipzig.remove(m);
        }
    }
}