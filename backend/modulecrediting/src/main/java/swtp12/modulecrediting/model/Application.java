package swtp12.modulecrediting.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
 public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullStatus; //enum?
    @CreationTimestamp
    private Date creationDate;
    private Date decisionDate;
    private String course;

    @OneToMany(targetEntity = ModulesConnection.class , mappedBy = "application" , cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private List<ModulesConnection> modulesConnections = new ArrayList<ModulesConnection>();


    public Application(String fullStatus, Date creationDate, Date decisionDate, String course) {
        this.fullStatus = fullStatus;
        this.creationDate = creationDate;
        this.decisionDate = decisionDate;
        this.course = course;
    }
}