package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CourseLeipzig {@link Entity}. Includes custom {@link JsonView} classes
 * 
 * @see JsonView
 * @see Views
 * @see Views.ApplicationLoginOverview
 * @see Views.ApplicationStudent
 * @see Views.CoursesWithModules
 * @see Views.RelatedModulesConnection
 */
@Data
@NoArgsConstructor
@Entity
public class CourseLeipzig {
    @Id
    @GeneratedValue
    private Long id;

    @JsonView({Views.CoursesWithModules.class, Views.ApplicationStudent.class, Views.ApplicationLoginOverview.class, Views.RelatedModulesConnection.class})
    @NotBlank(message = "Name may not be blank")
    @Column(unique = true, nullable = false)
    private String name;
    @JsonView({Views.CoursesWithModules.class})
    private Boolean isActive;

    //Relation CourseLeipzig <-> ModuleLeipzig
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_leipzig_module_leipzig",
            joinColumns = @JoinColumn(name = "course_leipzig_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    @JsonView(Views.CoursesWithModules.class)
    private List<ModuleLeipzig> modulesLeipzigCourse = new ArrayList<>();

    //Relation CourseLeipzig <-> Application
    @OneToMany(mappedBy = "courseLeipzig")
    @JsonBackReference
    private List<Application> applications = new ArrayList<>();


    /**
     * Constructor for {@link CourseLeipzig}.
     * <p>Creates {@link CourseLeipzig} with given {@link String} as {@link #name}
     * and sets {@link #isActive} to {@link Boolean true}.
     * 
     * @see CourseLeipzig
     */
    public CourseLeipzig(String name) {
        this.name = name;
        this.isActive = true;
    }


    /**
     * Adds the given {@link ModuleLeipzig} to this {@link CourseLeipzig}.
     * <p>Also adds this {@link CourseLeipzig} to the given {@link ModuleLeipzig}.
     * 
     * @see CourseLeipzig
     * @see ModuleLeipzig
     */
    public void addModulesLeipzig(ModuleLeipzig moduleLeipzig) {
        this.modulesLeipzigCourse.add(moduleLeipzig);
        moduleLeipzig.getCoursesLeipzig().add(this);
    }

    /**
     * Adds the given {@link List} of {@link ModuleLeipzig} to this {@link CourseLeipzig}.
     * <p>Also adds this {@link CourseLeipzig} to from every {@link ModuleLeipzig} in the given {@link List}.
     * 
     * @see CourseLeipzig
     * @see ModuleLeipzig
     */
    public void addModulesLeipzig(List<ModuleLeipzig> moduleLeipzigs) {
        this.modulesLeipzigCourse.addAll(moduleLeipzigs);
        for (ModuleLeipzig moduleLeipzig : moduleLeipzigs) {
            moduleLeipzig.getCoursesLeipzig().add(this);
        }
    }

    /**
     * Removes the given {@link ModuleLeipzig} from this {@link CourseLeipzig}.
     * <p>Also removes this {@link CourseLeipzig} from the given {@link ModuleLeipzig}.
     * 
     * @see CourseLeipzig
     * @see ModuleLeipzig
     */
    public void removeModuleLeipzig(ModuleLeipzig moduleLeipzig) {
        this.modulesLeipzigCourse.remove(moduleLeipzig);
        moduleLeipzig.getCoursesLeipzig().remove(this);
    }

    /**
     * Removes the given {@link List} of {@link ModuleLeipzig} from this {@link CourseLeipzig}.
     * <p>Also removes this {@link CourseLeipzig} from from every {@link ModuleLeipzig} in the given {@link List}.
     * 
     * @see CourseLeipzig
     * @see ModuleLeipzig
     */
    public void removeModulesLeipzig(List<ModuleLeipzig> moduleLeipzigs) {
        this.modulesLeipzigCourse.removeAll(moduleLeipzigs);
        for (ModuleLeipzig moduleLeipzig : moduleLeipzigs) {
            moduleLeipzig.getCoursesLeipzig().remove(this);
        }
    }

    public void removeModulesLeipzig() {
        for (ModuleLeipzig moduleLeipzig : modulesLeipzigCourse) {
            moduleLeipzig.getCoursesLeipzig().remove(this);
        }
        modulesLeipzigCourse.clear();
    }
}
