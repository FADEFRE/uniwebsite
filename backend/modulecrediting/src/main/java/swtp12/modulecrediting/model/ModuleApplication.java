package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ModuleApplication {   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer points;
    private String pointSystem;
    private String university;
    private String commentApplicant;

    //Relation ModuleApplication <-> ModulesConnection (Setter in ModulesConnection)
    @OneToOne(targetEntity = ModulesConnection.class , mappedBy = "moduleApplication")
    @JsonIgnore
    private ModulesConnection modulesConnection;

    //Relation ModuleApplication <-> PdfDocument
    @OneToOne(targetEntity = PdfDocument.class , cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "pdf_document_id")
    private PdfDocument pdfDocument;

    public ModuleApplication(String name, Integer points, String pointSystem, String university, String commentApplicant) {
        this.name = name;
        this.points = points;
        this.pointSystem = pointSystem;
        this.university = university;
        this.commentApplicant = commentApplicant;
    }


    //Setter for Relation: ModuleApplication <-> ModulesConnection
    public void setPdfDocument(PdfDocument pdfDocument) {
        pdfDocument.setModuleApplication(this);
        this.pdfDocument = pdfDocument;
    }
}
