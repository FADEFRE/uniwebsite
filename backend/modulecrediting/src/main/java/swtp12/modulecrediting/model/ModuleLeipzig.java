package swtp12.modulecrediting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ModuleLeipzig {   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String number;
    private String studiengang;

    public ModuleLeipzig(String name, String number, String studiengang) {
        this.name = name;
        this.number = number;
        this.studiengang = studiengang;
    }

    public ModuleLeipzig() {
    }

}
