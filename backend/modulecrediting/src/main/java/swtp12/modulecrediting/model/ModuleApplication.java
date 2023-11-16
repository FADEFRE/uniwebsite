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
public class ModuleApplication {   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int points;
    private String point_System;
    private String university;
    private String[] bemerkung_Antragsteller;
    private int pdf_id;

    public ModuleApplication(String name, int points, String point_System, String university, String[] bemerkung_Antragsteller, int pdf_id) {
        this.name = name;
        this.points = points;
        this.point_System = point_System;
        this.university = university;
        this.bemerkung_Antragsteller = bemerkung_Antragsteller;
        this.pdf_id = pdf_id;
    }

    public ModuleApplication() {
    }
    
}
