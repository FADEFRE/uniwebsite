package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
 public class ModulesConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String decision; //Enum maybe
    private String comment;

    @ManyToOne(targetEntity = Application.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;


    @OneToOne(targetEntity = ModuleApplication.class , fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "moduleApplications_id" , referencedColumnName = "id")
    private ModuleApplication moduleApplications;

    @ManyToMany(targetEntity = ModuleLeipzig.class , fetch =  FetchType.LAZY)
    @JoinColumn(name = "moduleLeipzigs_id", referencedColumnName = "id")
    private List<ModuleLeipzig> moduleLeipzigs = new ArrayList<ModuleLeipzig>();
    

    public ModulesConnection(String decision, String comment) {
        this.decision = decision;
        this.comment = comment;
    }
}