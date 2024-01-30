package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
public class PdfDocument {
    @Id
    @GeneratedValue
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class})
    private Long id;
    @JsonView({Views.ApplicationStudent.class,Views.ApplicationLogin.class})
    private String name;
    @Lob
    @JsonIgnore
    private byte[] pdfData;

    //Relation PdfDocument <-> ExternalModule (Setter in ExternalModule)
    @OneToOne(mappedBy = "pdfDocument")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private ExternalModule externalModule;


    //Constructer
    public PdfDocument(String name, byte[] pdfData) {
        this.name = name;
        this.pdfData = pdfData;
    }
}
