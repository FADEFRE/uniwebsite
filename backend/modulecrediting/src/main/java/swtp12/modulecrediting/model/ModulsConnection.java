package swtp12.modulecrediting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/*
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;*/

@Entity
 public class ModulsConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String decision; //Enum maybe
    private String[] bemerkung;
    private int modul_Antrag;
    private int[] modul_Leipzig;
    private int final_modul_Leipzig;
    
    public ModulsConnection(Long id, String decision, String[] bemerkung, int modul_Antrag, int[] modul_Leipzig,
            int final_modul_Leipzig) {
        this.id = id;
        this.decision = decision;
        this.bemerkung = bemerkung;
        this.modul_Antrag = modul_Antrag;
        this.modul_Leipzig = modul_Leipzig;
        this.final_modul_Leipzig = final_modul_Leipzig;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String[] getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String[] bemerkung) {
        this.bemerkung = bemerkung;
    }

    public int getModul_Antrag() {
        return modul_Antrag;
    }

    public void setModul_Antrag(int modul_Antrag) {
        this.modul_Antrag = modul_Antrag;
    }

    public int[] getModul_Leipzig() {
        return modul_Leipzig;
    }

    public void setModul_Leipzig(int[] modul_Leipzig) {
        this.modul_Leipzig = modul_Leipzig;
    }

    public int getFinal_modul_Leipzig() {
        return final_modul_Leipzig;
    }

    public void setFinal_modul_Leipzig(int final_modul_Leipzig) {
        this.final_modul_Leipzig = final_modul_Leipzig;
    }

    public ModulsConnection() {
    }

 }