package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;


@Data
@Entity
public class ModulesConnection {
    @Id
    @GeneratedValue
    @JsonView(Views.ApplicationLogin.class)
    private Long id;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotNull(message = "decisionFinal must not be null")
    private EnumModuleConnectionDecision decisionFinal;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotNull(message = "commentDecision must not be null")
    private String commentDecision;

    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotNull(message = "asExamCertificate must not be null")
    private Boolean asExamCertificate;
    @JsonView(Views.ApplicationLogin.class)
    @NotNull(message = "decisionSuggestion must not be null")
    private EnumModuleConnectionDecision decisionSuggestion;
    @JsonView(Views.ApplicationStudent.class)
    @NotNull(message = "commentStudyOffice must not be null")
    private String commentStudyOffice;

    //Relation ModulesConnection <-> ModuleApplication (Setter in ModuleApplication)
    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    private ModuleApplication moduleApplication;

    //Relation ModulesConnection <-> ModuleLeipzig
    @ManyToMany
    @JoinTable(
            name = "module_connection_module_leipzig",
            joinColumns = @JoinColumn(name = "module_connection_id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id")
    )
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    private List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();

    //Relation ModulesConnection <-> Application (Setter in Application)
    @ManyToOne
    @JsonView(Views.RelatedModulesConnection.class)
    private Application application;


    public ModulesConnection() {
        decisionFinal = UNBEARBEITET;
        decisionSuggestion = UNBEARBEITET;
        commentDecision = "";
        commentStudyOffice = "";
        asExamCertificate = false;
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