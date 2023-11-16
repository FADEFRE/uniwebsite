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
 public class ModulesConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String decision; //Enum maybe
    private String[] bemerkung;
    private int modul_Antrag;
    private int[] modul_Leipzig;
    
    public ModulesConnection(String decision, String[] bemerkung, int modul_Antrag, int[] modul_Leipzig) {
        this.decision = decision;
        this.bemerkung = bemerkung;
        this.modul_Antrag = modul_Antrag;
        this.modul_Leipzig = modul_Leipzig;
    }

    public ModulesConnection() {
    }

 }