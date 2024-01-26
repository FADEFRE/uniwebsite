package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
public class CourseLeipzig {
    @Id
    @JsonView({
        Views.coursesWithModules.class, 
        Views.ApplicationOverview.class,
        Views.RelatedModulesConnection.class})
    @NotBlank(message = "Name may not be blank")
    private String name;
    @JsonView({Views.coursesWithModules.class})
    private Boolean isActive;

    //Relation CourseLeipzig <-> ModuleLeipzig
    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_leipzig_module_leipzig",
            joinColumns = @JoinColumn(name = "course_leipzig", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig", referencedColumnName = "name")
    )
    @JsonManagedReference
    @JsonView(Views.coursesWithModules.class)
    private List<ModuleLeipzig> modulesLeipzigCourse = new ArrayList<>();

    //Relation CourseLeipzig <-> Application
    @OneToMany(mappedBy = "courseLeipzig")
    @JsonBackReference
    private List<Application> applications = new ArrayList<>();


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
