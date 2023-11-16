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
public class Pdf {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int refNumber;
    private byte[] pdfData;

    public Pdf(String name, int refNumber, byte[] pdfData) {
        this.name = name;
        this.refNumber = refNumber;
        this.pdfData = pdfData;
    }

    public Pdf() {
    } 
}
