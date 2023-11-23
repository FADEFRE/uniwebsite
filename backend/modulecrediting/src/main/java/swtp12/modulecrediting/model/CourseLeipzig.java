package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CourseLeipzig {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "courseLeipzig")
    @JsonBackReference
    private List<Application> applications = new ArrayList<>();

    //Realtion CourseLeipzig <-> ModuleLeipzig
    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_leipzig_module_leipzig",
            joinColumns = @JoinColumn(name = "course_leipzig_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "module_leipzig_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    private List<ModuleLeipzig> modulesLeipzigCourse = new ArrayList<>();


    public CourseLeipzig(String name) {
        this.name = name;
    }

    //Function to add Course to Module and vice versa
    public void addCourseToModulesLeipzig(ModuleLeipzig moduleLeipzig) {
        this.modulesLeipzigCourse.add(moduleLeipzig);
        moduleLeipzig.getCoursesLeipzig().add(this);
    }

    public void addApplication(Application application) {
        applications.add(application);
    }
}
