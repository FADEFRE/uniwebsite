package swtp12.modulecrediting.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
 public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String full_status; //enum?
    private Date creation_date;
    private Date decision_date;
    private String studiengang;
    private int[] modulsConnectionID;

    public Application(String full_status, Date creation_date, Date decision_date, String studiengang, int[] modulsConnectionID) {
        this.full_status = full_status;
        this.creation_date = creation_date;
        this.decision_date = decision_date;
        this.studiengang = studiengang;
        this.modulsConnectionID = modulsConnectionID;
    }

    public Application() {
    }
 }