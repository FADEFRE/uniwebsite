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


    //Function to add a List of ModulesConnection to this ModuleLeipzig (and add this ModuleLeipzig to these ModuleConnections)
    public void addModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.getModulesLeipzig().add(this);
        }
        this.modulesConnections = modulesConnections;
    }
    //Function to add a ModulesConnection to this ModuleLeipzig (and add this ModuleLeipzig to the ModuleConnection)
    public void addModulesConnection(ModulesConnection mc) {
        mc.getModulesLeipzig().add(this);
        modulesConnections.add(mc);
    }

    //Dataloader Function to add Courses to this ModuelLeipzig
    public void addDataloaderOnlyCourses(List<String> dtLC) {
        DataloaderOnlyCourses.addAll(dtLC);
    }
}
