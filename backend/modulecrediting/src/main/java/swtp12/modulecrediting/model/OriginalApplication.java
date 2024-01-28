package swtp12.modulecrediting.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class OriginalApplication {
    
    @Id
    private String id;

    private EnumApplicationStatus fullStatus;
    
    @CreationTimestamp
    private LocalDateTime creationDate;

    private LocalDateTime lastEditedDate;

    private LocalDateTime decisionDate;

    //Relation Application <-> CourseLeipzig
    @ManyToOne
    @JoinColumn(name = "course_leipzig_id")
    @JsonManagedReference
    @JsonView({Views.ApplicationOverview.class,Views.RelatedModulesConnection.class})
    @JsonProperty("courseLeipzig")
    private CourseLeipzig originalCourseLeipzig;

    //Relation Application <-> ModulesConnection
    @OneToMany(mappedBy = "originalApplication", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationOverview.class})
    private List<ModulesConnection> modulesConnections = new ArrayList<>();


    public OriginalApplication(String id) {
        this.id = id;
        this.fullStatus = EnumApplicationStatus.IN_BEARBEITUNG;
    }


    //Function for setting a List of ModuelsConnections to this Application (and add the Application to all ModulesConnection in the List)
    public void setModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.setOriginalApplication(this);
        }
        this.modulesConnections = modulesConnections;
    }

    //Function for adding a Course to this Application (and add the Application to the Course aswell)
    public void setCourseLeipzig(CourseLeipzig courseLeipzig) {
        courseLeipzig.getOriginalApplications().add(this);
        this.originalCourseLeipzig = courseLeipzig;
    }
}

