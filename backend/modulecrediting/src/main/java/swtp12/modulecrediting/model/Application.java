package swtp12.modulecrediting.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
 public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullStatus; //enum?
    private Date creationDate;
    private Date decisionDate;

    // welcher kurs wird hier gespeichert?
    private String course;
    //private List<ModulesConnection> modulesConnectionList;


    public Application(String fullStatus, Date creationDate, Date decisionDate, String course) {
        this.fullStatus = fullStatus;
        this.creationDate = creationDate;
        this.decisionDate = decisionDate;
        this.course = course;
    }
}