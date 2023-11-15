package swtp12.modulecrediting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ModulAntrag {   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int points;
    private String point_System;
    private String university;
    private String[] bemerkung_Antragsteller;
    private int pdf_id;

    public ModulAntrag(Long id, String name, int points, String point_System, String university,
            String[] bemerkung_Antragsteller, int pdf_id) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.point_System = point_System;
        this.university = university;
        this.bemerkung_Antragsteller = bemerkung_Antragsteller;
        this.pdf_id = pdf_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPoint_System() {
        return point_System;
    }

    public void setPoint_System(String point_System) {
        this.point_System = point_System;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String[] getBemerkung_Antragsteller() {
        return bemerkung_Antragsteller;
    }

    public void setBemerkung_Antragsteller(String[] bemerkung_Antragsteller) {
        this.bemerkung_Antragsteller = bemerkung_Antragsteller;
    }

    public int getPdf_id() {
        return pdf_id;
    }

    public void setPdf_id(int pdf_id) {
        this.pdf_id = pdf_id;
    }

    public ModulAntrag() {
    }
    
}
