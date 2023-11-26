package swtp12.modulecrediting.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private Long id;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = generateRandomUniqueId();
        }
    }

    private Long generateRandomUniqueId() {
        // Custom logic to generate a random long ID
        Random random = new Random();
        long uniqueId = System.currentTimeMillis(); // Use current timestamp as part of the ID
        uniqueId = uniqueId * 1000 + random.nextInt(1000); // Append a random number

        return uniqueId;
    }

    private ApplicationStatus fullStatus;
    @CreationTimestamp
    private LocalDate creationDate;
    private LocalDate decisionDate;

    //Relation Application <-> CourseLeipzig
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