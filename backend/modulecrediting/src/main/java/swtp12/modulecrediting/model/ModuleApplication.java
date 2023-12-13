package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@Entity
public class ModuleApplication {   
    @Id
    @GeneratedValue
    private Long id;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotBlank(message = "module name must not be blank (empty String)")
    private String name;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotNull(message = "points must not be null")
    private Integer points;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotNull(message = "point system must not be null")
    private String pointSystem;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotBlank(message = "university must not be blank (empty String)")
    private String university;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotNull(message = "comment applicant must not be null")
    private String commentApplicant;

    //Relation ModuleApplication <-> PdfDocument
    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    private PdfDocument pdfDocument;

    //Relation ModuleApplication <-> ModulesConnection (Setter in ModulesConnection)
    @OneToOne(mappedBy = "moduleApplication")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private ModulesConnection modulesConnection;



    public ModuleApplication(String name, Integer points, String pointSystem, String university, String commentApplicant) {
        this.name = name;
        this.points = points;
        this.pointSystem = pointSystem;
        this.university = university;
        this.commentApplicant = commentApplicant;
    }


    //Function to add a PDF Document to this ModuleApplication (adds this Module to the PDF aswell)
    public void addPdfDocument(PdfDocument pdfDocument) {
        pdfDocument.setModuleApplication(this);
        this.pdfDocument = pdfDocument;
    }
}
