package swtp12.modulecrediting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ModuleApplication {   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer points;
    private String pointSystem;

    private String university;

    private List<String> commentApplicant;

    //private PdfDocument pdfDocument;


    public ModuleApplication(String name, Integer points, String pointSystem, String university, List<String> commentApplicant) {
        this.name = name;
        this.points = points;
        this.pointSystem = pointSystem;
        this.university = university;
        this.commentApplicant = commentApplicant;
    }
}
