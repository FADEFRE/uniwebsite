package swtp12.modulecrediting.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import lombok.NoArgsConstructor;


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
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private LocalDateTime creationDate;
    @JsonView({Views.ApplicationOverview.class})
    private LocalDateTime lastEditedDate;
    @JsonView({Views.ApplicationOverview.class})
    private LocalDateTime decisionDate;

    //Relation Application <-> CourseLeipzig
    @ManyToOne
    @JoinColumn(name = "course_leipzig_id")
    @JsonManagedReference
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    private CourseLeipzig courseLeipzig;

    //Relation Application <-> ModulesConnection
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonView(Views.ApplicationStudent.class)
    private List<ModulesConnection> modulesConnections = new ArrayList<>();


    public Application(String id) {
        this.id = id;
        this.fullStatus = EnumApplicationStatus.NEU;
    }


    //Function for setting a List of ModuelsConnections to this Application (and add the Application to all ModulesConnection in the List)
    public void setModulesConnections(List<ModulesConnection> modulesConnections) {
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
