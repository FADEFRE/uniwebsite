package swtp12.modulecrediting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PdfDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private byte[] pdfData;

    // @OneToOne
    //private ModuleApplication moduleApplication;


    public PdfDocument(String name, byte[] pdfData) {
        this.name = name;
        this.pdfData = pdfData;
    }
}
