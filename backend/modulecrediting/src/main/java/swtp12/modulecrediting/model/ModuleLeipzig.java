package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
public class ModuleLeipzig {   
    @Id
    @GeneratedValue
    private Long id;

    @JsonView({Views.coursesWithModules.class, Views.modulesWithoutCourse.class, Views.ApplicationStudent.class})
    private String moduleName;
    @JsonView({Views.coursesWithModules.class, Views.modulesWithoutCourse.class})
    private String moduleCode;
    @JsonIgnore
    private List<String> DataloaderOnlyCourses;

    //Relation ModuleLeipzig <-> ModulesConnection
    @ManyToMany(mappedBy = "modulesLeipzig")
    @JsonBackReference
    private List<ModulesConnection> modulesConnections = new ArrayList<>();

    //Relation ModuleLeipzig <-> CourseLeipzig
    @ManyToMany(mappedBy = "modulesLeipzigCourse", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private List<CourseLeipzig> coursesLeipzig = new ArrayList<>();


    public ModuleLeipzig(String moduleName, String moduleCode, List<String> courses) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.DataloaderOnlyCourses = courses;
    }


    //Dataloader Function to add Courses to this ModuelLeipzig
    public void addDataloaderOnlyCourses(List<String> dtLC) {
        DataloaderOnlyCourses.addAll(dtLC);
    }
}
