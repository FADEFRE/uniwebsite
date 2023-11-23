package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ModuleLeipzig {   
    @Id
    @GeneratedValue
    private Long id;

    private String moduleName;
    private String moduleCode;
    private List<String> courses;

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
        this.courses = courses;
    }


    //Setter for Relation: ModuleLeipzig <-> ModulesConnection
    public void setModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.getModulesLeipzig().add(this);
        }
        this.modulesConnections = modulesConnections;
    }
    // add (Setter) for Relation: ModuleLeipzig <-> ModulesConnection
    public void addModulesConnection(ModulesConnection modulesConnection) {
        modulesConnections.add(modulesConnection);
    }

    public void addCourseNames(List<String> courseName) {
        courses.addAll(courseName);
    }
}
