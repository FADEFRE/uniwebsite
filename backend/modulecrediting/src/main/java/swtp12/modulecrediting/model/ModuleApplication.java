package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;

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
    @JsonView(Views.ApplicationLogin.class)
    private Long id;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotBlank(message = "moduleName must not be blank (empty String)")
    private String name;
    @JsonView({Views.ApplicationStudent.class})
    @NotNull(message = "points must not be null")
    private Integer points;
    @JsonView({Views.ApplicationStudent.class})
    @NotNull(message = "point system must not be null")
    private String pointSystem;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class})
    @NotBlank(message = "university must not be blank (empty String)")
    private String university;


    //Relation ModuleApplication <-> PdfDocument
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class})
    private PdfDocument pdfDocument;

    //Relation ModuleApplication <-> ModulesConnection (Setter in ModulesConnection)
    @ManyToOne
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private ModulesConnection modulesConnection;


    //Function to set the PDF Document to this ModuleApplication (adds this Module to the PDF aswell)
    public void setPdfDocument(PdfDocument pdfDocument) {
        pdfDocument.setModuleApplication(this);
        this.pdfDocument = pdfDocument;
    }
}
