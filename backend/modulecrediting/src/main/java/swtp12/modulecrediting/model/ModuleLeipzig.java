package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
public class ModuleLeipzig {   
    @Id
    @JsonView({Views.coursesWithModules.class, Views.modulesWithoutCourse.class, Views.ApplicationStudent.class, Views.RelatedModulesConnection.class})
    private String moduleName;
    @JsonView({Views.coursesWithModules.class, Views.modulesWithoutCourse.class})
    private String moduleCode;

    //Relation ModuleLeipzig <-> ModulesConnection
    @ManyToMany(mappedBy = "modulesLeipzig")
    @JsonBackReference
    private List<ModulesConnection> modulesConnections = new ArrayList<>();

    //Relation ModuleLeipzig <-> CourseLeipzig
    @ManyToMany(mappedBy = "modulesLeipzigCourse", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private List<CourseLeipzig> coursesLeipzig = new ArrayList<>();


    public ModuleLeipzig(String moduleName, String moduleCode) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
    }

}
