package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ModulesConnection {@link Entity}. Includes custom {@link JsonView} classes
 * 
 * @see JsonView
 * @see Views
 * @see Views.ApplicationLogin
 * @see Views.ApplicationLoginOverview
 * @see Views.ApplicationStudent
 * @see Views.RelatedModulesConnection
 */
@Data
@Entity
public class ModulesConnection {
    @Id
    @GeneratedValue
    @JsonView({Views.ApplicationStudent.class, Views.ApplicationLogin.class, Views.RelatedModulesConnection.class})
    private Long id;

    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class, Views.RelatedModulesConnection.class})
    @NotNull(message = "decisionFinal must not be null")
    private EnumModuleConnectionDecision decisionFinal;
    @JsonView({Views.ApplicationStudent.class, Views.ApplicationLogin.class})
    @NotNull(message = "commentDecision must not be null")
    private String commentDecision;
    @JsonView(Views.ApplicationLogin.class)
    @NotNull(message = "decisionSuggestion must not be null")
    private EnumModuleConnectionDecision decisionSuggestion;
    @JsonView({Views.ApplicationLogin.class})
    @NotNull(message = "commentStudyOffice must not be null")
    private String commentStudyOffice;
    @JsonView({Views.ApplicationStudent.class, Views.ApplicationLogin.class})
    @NotNull(message = "formalRejection must not be null")
    private Boolean formalRejection;
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class})
    @NotNull(message = "formalRejectionComment must not be null")
    private String formalRejectionComment;

    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class})
    @NotNull(message = "comment applicant must not be null")
    private String commentApplicant;

    //Relation ModulesConnection <-> ExternalModule (Setter in ExternalModule)
    @OneToMany(mappedBy = "modulesConnection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class,Views.ApplicationLoginOverview.class})
    private List<ExternalModule> externalModules = new ArrayList<>();

    //Relation ModulesConnection <-> ModuleLeipzig
    @ManyToMany
    @JoinTable(
            name = "module_connection_module_leipzig",
            joinColumns = @JoinColumn(name = "module_connection_id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id")
    )
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class, Views.RelatedModulesConnection.class})
    private List<ModuleLeipzig> modulesLeipzig = new ArrayList<>();

    //Relation ModulesConnection <-> Application (Setter in Application)
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JsonView(Views.RelatedModulesConnection.class)
    private Application application;

    private Boolean isOriginalModulesConnection;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.ApplicationStudent.class)
    private ModulesConnection modulesConnectionOriginal;

    /**
     * Constructor for {@link ModulesConnection}. Creates {@link ModulesConnection} and sets {@link #decisionFinal} and {@link #decisionFinal} to {@link EnumModuleConnectionDecision unedited}.
     * Sets {@link #formalRejection} and {@link #isOriginalModulesConnection} to false;
     * @see ModulesConnection
     * @see EnumModuleConnectionDecision
     */
    public ModulesConnection() {
        decisionFinal = EnumModuleConnectionDecision.unedited;
        decisionSuggestion = EnumModuleConnectionDecision.unedited;
        commentDecision = "";
        commentStudyOffice = "";
        formalRejection = false;
        formalRejectionComment = "";
        isOriginalModulesConnection = false;
    }


    /**
     * Sets the given {@link List} of {@link ExternalModule ExternalModules} to this {@link ModulesConnection}.
     * <p>Also sets this {@link ModulesConnection} for every {@link ExternalModule} in the given {@link List}.
     * 
     * @see ExternalModule
     * @see ModulesConnection 
     */
    public void setExternalModules(List<ExternalModule> externalModules) {
        for(ExternalModule m : externalModules) {
            m.setModulesConnection(this);
        }
        this.externalModules = externalModules;
    }

    /**
     * Adds the given {@link List} of {@link ExternalModule ExternalModules} to the existing List of {@link ExternalModule ExternalModules} of this {@link ModulesConnection}.
     * <p>Also sets this {@link ModulesConnection} for every {@link ExternalModule} in the given {@link List}.
     * 
     * @see ExternalModule
     * @see ModulesConnection 
     */
    public void addExternalModules(List<ExternalModule> externalModules) {
        for(ExternalModule m : externalModules) {
            m.setModulesConnection(this);
            this.externalModules.add(m);
        }
    }

    /**
     * Removes the given {@link List} of {@link ExternalModule ExternalModules} from this {@link ModulesConnection}.
     * <p>Also removes this {@link ModulesConnection} from from every {@link ExternalModule} in the given {@link List}.
     * 
     * @see ModulesConnection
     * @see ModuleLeipzig
     */
    public void removeExternalModules(List<ExternalModule> externalModules) {
        for(ExternalModule m : externalModules) {
            m.setModulesConnection(null);
            this.externalModules.remove(m);
        }
    }

    /**
     * Sets the given {@link List} of {@link ModuleLeipzig ModulesLeipzig} to this {@link ModulesConnection}.
     * 
     * @see ModuleLeipzig
     * @see ModulesConnection 
     */
    public void setModulesLeipzig(List<ModuleLeipzig> modulesLeipzig) {
        this.modulesLeipzig = modulesLeipzig;
    }

    /**
     * Adds the given {@link List} of {@link ModuleLeipzig ModulesLeipzig} to the existing List of {@link ModuleLeipzig ModulesLeipzig} of this {@link ModulesConnection}.
     * 
     * @see ModuleLeipzig
     * @see ModulesConnection 
     */
    public void addModulesLeipzig(List<ModuleLeipzig> modulesLeipzig) {
        this.modulesLeipzig.addAll(modulesLeipzig);
    }

    /**
     * Removes the given {@link List} of {@link ModuleLeipzig ModulesLeipzig} of this {@link ModulesConnection}.
     * @see ModuleLeipzig
     * @see ModulesConnection
     */
    public void removeModulesLeipzig(List<ModuleLeipzig> modulesLeipzig) {
        this.modulesLeipzig.removeAll(modulesLeipzig);
    }

    /**
     * Removes all {@link ModuleLeipzig ModulesLeipzig} of this {@link ModulesConnection}.
     * @see ModuleLeipzig
     * @see ModulesConnection
     */
    public void removeAllModulesLeipzig() {
        this.modulesLeipzig.clear();
    }
}