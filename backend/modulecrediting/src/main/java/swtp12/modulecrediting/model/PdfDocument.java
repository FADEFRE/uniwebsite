package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
public class PdfDocument {
    @Id
    @GeneratedValue
    @JsonView(Views.ApplicationStudent.class)
    private Long id;
    @JsonView(Views.ApplicationStudent.class)
    private String name;
    @Lob
    @JsonIgnore
    private byte[] pdfData;

    //Relation PdfDocument <-> ModuleApplication (Setter in ModuleApplication)
    @OneToOne(mappedBy = "pdfDocument")
    @JsonBackReference
    private ModuleApplication moduleApplication;


    public PdfDocument(String name, byte[] pdfData) {
        this.name = name;
        this.pdfData = pdfData;
    }

}
