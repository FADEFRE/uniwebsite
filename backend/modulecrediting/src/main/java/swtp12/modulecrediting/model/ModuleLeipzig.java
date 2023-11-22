package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
    @JsonIgnore
    private List<ModulesConnection> modulesConnections = new ArrayList<>();

    //Relation ModuleLeipzig <-> CourseLeipzig
    @ManyToMany(mappedBy = "modulesLeipzigCourse", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<CourseLeipzig> courseSLeipzig = new ArrayList<>();


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

    public void addCourseNames(List<String> courseName) {
        courses.addAll(courseName);
    }
}
