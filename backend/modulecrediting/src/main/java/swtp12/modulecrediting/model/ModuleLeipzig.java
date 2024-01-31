package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * ModuleLeipzig {@link Entity}. Includes custom {@link JsonView} classes
 * 
 * @see JsonView
 * @see Views
 * @see Views.ApplicationLogin
 * @see Views.ApplicationStudent
 * @see Views.CoursesWithModules
 * @see Views.ModulesWithoutCourse
 * @see Views.RelatedModulesConnection
 */
@Data
@NoArgsConstructor
@Entity
public class ModuleLeipzig {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @JsonView({
        Views.CoursesWithModules.class, 
        Views.ModulesWithoutCourse.class, 
        Views.ApplicationStudent.class,
        Views.RelatedModulesConnection.class,
        Views.ApplicationLogin.class
    })
    @Column(unique = true, nullable = false)
    private String name;
    @JsonView({Views.CoursesWithModules.class, Views.ModulesWithoutCourse.class})
    private String code;
    @JsonView({Views.CoursesWithModules.class, Views.ModulesWithoutCourse.class})
    private Boolean isActive;

    //Relation ModuleLeipzig <-> ModulesConnection
    @ManyToMany(mappedBy = "modulesLeipzig")
    @JsonBackReference
    private List<ModulesConnection> modulesConnections = new ArrayList<>();

    //Relation ModuleLeipzig <-> CourseLeipzig
    @ManyToMany(mappedBy = "modulesLeipzigCourse", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private List<CourseLeipzig> coursesLeipzig = new ArrayList<>();


    /**
     * Constructor for {@link ModuleLeipzig}.
     * <p>Creates {@link ModuleLeipzig} with given {@link String name} and {@link String code}
     * and sets {@link #isActive} to {@link Boolean true}.
     * 
     * @see ModuleLeipzig
     */
    public ModuleLeipzig(String name, String code) {
        this.name = name;
        this.code = code;
        this.isActive = true;
    }
}
