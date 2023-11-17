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

    private String comment; // wieso array?


    // OneToOne
    //private ModuleApplication moduleApplication;


    // ManyToMany??
    //private List<ModuleLeipzig> moduleLeipzigList;


    public ModulesConnection(String decision, String comment) {
        this.decision = decision;
        this.comment = comment;
    }
}