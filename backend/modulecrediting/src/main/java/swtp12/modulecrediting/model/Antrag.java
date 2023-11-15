package swtp12.modulecrediting.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
 public class Antrag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String full_status; //enum?
    private Date creation_date;
    private Date decision_date;
    private String studiengang;
    private int[] modulsConnectionID;

    public Antrag(Long id, String full_status, Date creation_date, Date decision_date, String studiengang, int[] modulsConnectionID) {
        this.id = id;
        this.full_status = full_status;
        this.creation_date = creation_date;
        this.decision_date = decision_date;
        this.studiengang = studiengang;
        this.modulsConnectionID = modulsConnectionID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFull_status() {
        return full_status;
    }

    public void setFull_status(String full_status) {
        this.full_status = full_status;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getDecision_date() {
        return decision_date;
    }

    public void setDecision_date(Date decision_date) {
        this.decision_date = decision_date;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }

    public int[] getModulsConnectionID() {
        return modulsConnectionID;
    }

    public void setModulsConnectionID(int[] modulsConnectionID) {
        this.modulsConnectionID = modulsConnectionID;
    }

    public Antrag() {
    }
 }