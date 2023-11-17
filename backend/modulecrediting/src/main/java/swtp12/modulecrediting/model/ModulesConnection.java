package swtp12.modulecrediting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
 public class ModulesConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String decision; //Enum maybe

    @ElementCollection
    private List<String> comments; // wieso array?


    // OneToOne
    //private ModuleApplication moduleApplication;


    // ManyToMany??
    //private List<ModuleLeipzig> moduleLeipzigList;


    public ModulesConnection(String decision, List<String> comments) {
        this.decision = decision;
        this.comments = comments;
    }
}