package swtp12.modulecrediting.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(
        name = "graph.Application.modulesConnections",
        attributeNodes = @NamedAttributeNode(value = "modulesConnections")
)
 public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ApplicationStatus fullStatus;
    @CreationTimestamp
    private LocalDate creationDate;
    private LocalDate decisionDate;

    @ManyToOne
    @JoinColumn(name = "course_leipzig_id")
    @JsonManagedReference
    private CourseLeipzig courseLeipzig;

    //Relation Application <-> ModulesConnection
    @OneToMany(mappedBy = "application" , cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
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


    //Setter for Relation: Application <-> ModulesConnection
    public void setModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.setApplication(this);
        }
        this.modulesConnections = modulesConnections;
    }

    public void setCourseLeipzig(CourseLeipzig courseLeipzig) {
        courseLeipzig.addApplication(this);
        this.courseLeipzig = courseLeipzig;
    }
}