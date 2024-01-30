package swtp12.modulecrediting.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Application {@link Entity}. Includes custom {@link JsonView} classes
 * 
 * @see JsonView
 * @see Views
 * @see Views.ApplicationOverview
 * @see Views.RelatedModulesConnection
 */
@Data
@NoArgsConstructor
@Entity
public class Application {
    @Id
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private String id;

    @JsonView({Views.ApplicationOverview.class})
    private EnumApplicationStatus fullStatus;

    @CreationTimestamp
    @JsonView({Views.ApplicationOverview.class})
    private LocalDateTime creationDate;
    @JsonView({Views.ApplicationOverview.class})
    private LocalDateTime lastEditedDate;
    @JsonView({Views.ApplicationOverview.class, Views.RelatedModulesConnection.class})
    private LocalDateTime decisionDate;

    //Relation Application <-> CourseLeipzig
    @ManyToOne @JoinColumn(name = "course_leipzig_id")
    @JsonManagedReference 
    @JsonView({Views.ApplicationOverview.class, Views.RelatedModulesConnection.class})
    private CourseLeipzig courseLeipzig;

    //Relation Application <-> ModulesConnection
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationOverview.class})
    private List<ModulesConnection> modulesConnections = new ArrayList<>();

    
    /**
     * Constructor for {@link Application}.
     * <p>Creates {@link Application} with given {@link String} as {@link #id}
     * and sets {@link #fullStatus} to {@link EnumApplicationStatus NEU}.
     * 
     * @see Application
     * @see EnumApplicationStatus
     */
    public Application(String id) {
        this.id = id;
        this.fullStatus = EnumApplicationStatus.NEU;
    }


    // set modules connection + db relation
    public void setModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.setApplication(this);
        }
        this.modulesConnections = modulesConnections;
    }

    /**
     * Adds the given {@link List} of {@link ModulesConnection} to this {@link Application}.
     * <p>Also sets this {@link Application} for every {@link ModulesConnection} in the given {@link List}.
     * 
     * @see Application
     * @see ModulesConnection 
     */
    public void addModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) { 
            mc.setApplication(this); 
        }
        this.modulesConnections.addAll(modulesConnections);
    }

    /**
     * Set the given {@link CourseLeipzig} for this {@link Application}.
     * <p>Also adds this {@link Application} to the {@link List} of {@link Application Applications} in the given {@link CourseLeipzig}.
     * 
     * @see Application
     * @see CourseLeipzig 
     */
    public void setCourseLeipzig(CourseLeipzig courseLeipzig) {
        courseLeipzig.getApplications().add(this);
        this.courseLeipzig = courseLeipzig;
    }
}
