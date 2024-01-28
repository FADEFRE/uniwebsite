package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
public class CourseLeipzig {
    @Id
    @GeneratedValue
    private Long id;

    @JsonView({
        Views.coursesWithModules.class, 
        Views.ApplicationOverview.class,
        Views.RelatedModulesConnection.class})
    @NotBlank(message = "Name may not be blank")
    @Column(unique = true, nullable = false)
    private String name;
    @JsonView({Views.coursesWithModules.class})
    private Boolean isActive;

    //Relation CourseLeipzig <-> ModuleLeipzig
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_leipzig_module_leipzig",
            joinColumns = @JoinColumn(name = "course_leipzig_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    @JsonView(Views.coursesWithModules.class)
    private List<ModuleLeipzig> modulesLeipzigCourse = new ArrayList<>();

    //Relation CourseLeipzig <-> Application
    @OneToMany(mappedBy = "courseLeipzig")
    @JsonBackReference
    private List<Application> applications = new ArrayList<>();

    //Relation CourseLeipzig <-> OriginalApplication
    @OneToMany(mappedBy = "originalCourseLeipzig")
    @JsonBackReference
    private List<OriginalApplication> originalApplications = new ArrayList<>();


    public CourseLeipzig(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }


    //Function to add a Module to this Course (adds the Course to the Module aswell)
    public void addCourseToModulesLeipzig(ModuleLeipzig moduleLeipzig) {
        this.modulesLeipzigCourse.add(moduleLeipzig);
        moduleLeipzig.getCoursesLeipzig().add(this);
    }

    public void removeCourseToModulesLeipzig(ModuleLeipzig moduleLeipzig) {
        this.modulesLeipzigCourse.remove(moduleLeipzig);
        moduleLeipzig.getCoursesLeipzig().remove(this);
    }
}
