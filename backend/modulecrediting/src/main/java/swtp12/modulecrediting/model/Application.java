package swtp12.modulecrediting.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@NamedEntityGraph(
        name = "graph.Application.modulesConnections",
        attributeNodes = @NamedAttributeNode(value = "modulesConnections")
)
public class Application {


    @Id
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private String id;

    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private EnumApplicationStatus fullStatus;
    @CreationTimestamp
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private LocalDate creationDate;
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private LocalDate decisionDate;

    //Relation Application <-> CourseLeipzig
    @ManyToOne
    @JoinColumn(name = "course_leipzig_id")
    @JsonManagedReference
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private CourseLeipzig courseLeipzig;

    //Relation Application <-> ModulesConnection
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonView(Views.ApplicationStudent.class)
    private List<ModulesConnection> modulesConnections = new ArrayList<>();


    public Application(String id, EnumApplicationStatus fullStatus, LocalDate creationDate) {
        this.id = id;
        this.fullStatus = fullStatus;
        this.creationDate = creationDate;
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
