package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
public class CourseLeipzig {
    @Id
    @GeneratedValue
    private Long id;

    @JsonView({Views.coursesWithModules.class, Views.ApplicationOverview.class})
    @Column(unique = true)
    private String name;

    //Reltion CourseLeipzig <-> ModuleLeipzig
    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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


    public CourseLeipzig(String name) {
        this.name = name;
    }

    //Function to add a Module to this Course (adds the Course to the Module aswell)
    public void addCourseToModulesLeipzig(ModuleLeipzig moduleLeipzig) {
        this.modulesLeipzigCourse.add(moduleLeipzig);
        moduleLeipzig.getCoursesLeipzig().add(this);
    }
}
