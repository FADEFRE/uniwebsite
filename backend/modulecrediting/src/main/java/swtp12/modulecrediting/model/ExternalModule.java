package swtp12.modulecrediting.model;

import java.util.List;

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
public class ExternalModule {
    @Id
    @GeneratedValue
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class})
    private Long id;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class,Views.ApplicationOverview.class})
    @NotBlank(message = "moduleName must not be blank (empty String)")
    private String name;
    @JsonView({Views.ApplicationStudent.class})
    @NotNull(message = "points must not be null")
    private Integer points;
    @JsonView({Views.ApplicationStudent.class})
    @NotNull(message = "point system must not be null")
    private String pointSystem;
    @JsonView({Views.ApplicationStudent.class,Views.RelatedModulesConnection.class,Views.ApplicationOverview.class})
    @NotBlank(message = "university must not be blank (empty String)")
    private String university;


    //Relation ExternalModule <-> PdfDocument
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonView({Views.ApplicationStudent.class})
    private PdfDocument pdfDocument;

    //Relation ExternalModule <-> ModulesConnection (Setter in ModulesConnection)
    @ManyToMany(mappedBy = "externalModules")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private List<ModulesConnection> modulesConnection;


    //Function to set the PDF Document to this ExternalModule (adds this Module to the PDF aswell)
    public void setPdfDocument(PdfDocument pdfDocument) {
        pdfDocument.setExternalModule(this);
        this.pdfDocument = pdfDocument;
    }
}
