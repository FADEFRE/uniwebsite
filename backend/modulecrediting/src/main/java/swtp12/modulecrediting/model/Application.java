package swtp12.modulecrediting.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@Entity
@NamedEntityGraph(
        name = "graph.Application.modulesConnections",
        attributeNodes = @NamedAttributeNode(value = "modulesConnections")
)
 public class Application {
    @Id
    @GeneratedValue
    @JsonView(Views.ApplicationOverview.class)
    private Long id;
    @JsonView(Views.ApplicationOverview.class)
    private ApplicationStatus fullStatus;
    @CreationTimestamp
    @JsonView(Views.ApplicationOverview.class)
    private LocalDate creationDate;
    @JsonView(Views.ApplicationLogin.class)
    private LocalDate decisionDate;

    //Relation Application <-> CourseLeipzig
    @ManyToOne
    @JoinColumn(name = "course_leipzig_id")
    @JsonManagedReference
    @JsonView(Views.ApplicationOverview.class)
    private CourseLeipzig courseLeipzig;

    //Relation Application <-> ModulesConnection
    @OneToMany(mappedBy = "application" , cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonView(Views.ApplicationStudent.class)
    private List<ModulesConnection> modulesConnections = new ArrayList<>();


    public Application(ApplicationStatus fullStatus, LocalDate creationDate, LocalDate decisionDate) {
        this.fullStatus = fullStatus;
        this.creationDate = creationDate;
        this.decisionDate = decisionDate;
    }

    public enum ApplicationStatus{
        OFFEN,
        IN_BEARBEITUNG,
        ABGESCHLOSSEN
    }


    //Function for adding a List of ModuelsConnections to this Application (and add the Application to all ModulesConnection in the List)
    public void addModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.setApplication(this);
        }
        this.modulesConnections = modulesConnections;
    }

    //Function for adding a Course to this Application (and add the Application to the Course aswell)
    public void setCourseLeipzig(CourseLeipzig courseLeipzig) {
        courseLeipzig.getApplications().add(this);
        this.courseLeipzig = courseLeipzig;
    }
}