package swtp12.modulecrediting.model;

import java.time.LocalDate;
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
    private LocalDate creationDate;
    private LocalDate decisionDate;

    @OneToMany(mappedBy = "application" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<ModulesConnection> modulesConnections = new ArrayList<>();


    public Application(String fullStatus, LocalDate creationDate, LocalDate decisionDate) {
        this.fullStatus = fullStatus;
        this.creationDate = creationDate;
        this.decisionDate = decisionDate;
    }

    public void setModulesConnections(List<ModulesConnection> modulesConnections) {
        for(ModulesConnection mc : modulesConnections) {
            mc.setApplication(this);
        }

        this.modulesConnections = modulesConnections;
    }
}