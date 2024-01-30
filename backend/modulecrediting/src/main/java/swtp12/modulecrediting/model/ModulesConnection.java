package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static swtp12.modulecrediting.model.EnumModuleConnectionDecision.*;


@Data
@Entity
public class ModulesConnection {
    @Id
    @GeneratedValue
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class,Views.RelatedModulesConnection.class})
    private Long id;

    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotNull(message = "decisionFinal must not be null")
    private EnumModuleConnectionDecision decisionFinal;
    @JsonView({Views.ApplicationStudent.class})
    @NotNull(message = "commentDecision must not be null")
    private String commentDecision;
    @JsonView(Views.ApplicationLogin.class)
    @NotNull(message = "decisionSuggestion must not be null")
    private EnumModuleConnectionDecision decisionSuggestion;
    @JsonView(Views.ApplicationLogin.class)
    @NotNull(message = "commentStudyOffice must not be null")
    private String commentStudyOffice;
    @JsonView(Views.ApplicationStudent.class)
    @NotNull(message = "formalRejection must not be null")
    private Boolean formalRejection;
    @JsonView(Views.ApplicationStudent.class)

    @NotNull(message = "formalRejectionComment must not be null")
    private String formalRejectionComment;

    @JsonView({Views.ApplicationStudent.class})
    @NotNull(message = "comment applicant must not be null")
    private String commentApplicant;

    //Relation ModulesConnection <-> ExternalModule (Setter in ExternalModule)
    @OneToMany(mappedBy = "modulesConnection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class,Views.ApplicationOverview.class})
    private List<ExternalModule> externalModules = new ArrayList<>();

    //Relation ModulesConnection <-> ModuleLeipzig
    @ManyToMany
    @JoinTable(
            name = "module_connection_module_leipzig",
            joinColumns = @JoinColumn(name = "module_connection_id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id")
    )
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class, Views.RelatedModulesConnection.class})
    private List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();

    //Relation ModulesConnection <-> Application (Setter in Application)
    @ManyToOne
    @JsonView(Views.RelatedModulesConnection.class)
    @EqualsAndHashCode.Exclude
    private Application application;


    // TODO: add json views
    @OneToOne(cascade = CascadeType.ALL)
    @JsonView(Views.ApplicationLogin.class)
    private ModulesConnection modulesConnectionOriginal;


    public ModulesConnection() {
        decisionFinal = unedited;
        decisionSuggestion = unedited;
        commentDecision = "";
        commentStudyOffice = "";
        formalRejection = false;
        formalRejectionComment = "";
    }


    //Function to set List of ExternalModule to this ModulesConnection (and add this ModuleConnectio to all ExternalModule in the List)
    public void setExternalModules(List<ExternalModule> externalModules) {
        for(ExternalModule m : externalModules) {
            m.setModulesConnection(this);
        }
        this.externalModules = externalModules;
    }
    public void addExternalModules(List<ExternalModule> externalModules) {
        for(ExternalModule m : externalModules) {
            m.setModulesConnection(this);
            this.externalModules.add(m);
        }
    }
    public void removeExternalModules(List<ExternalModule> externalModules) {
        for(ExternalModule m : externalModules) {
            m.setModulesConnection(null);
            this.externalModules.remove(m);
        }
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
    public void removeAllModulesLeipzig() {
        for(ModuleLeipzig m : modulesLeipzig) {
            m.getModulesConnections().remove(this);
        }
        this.modulesLeipzig = new ArrayList<>();
    }
}