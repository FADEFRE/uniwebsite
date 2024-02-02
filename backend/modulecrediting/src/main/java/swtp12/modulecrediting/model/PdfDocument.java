package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * PdfDocument {@link Entity}. Includes custom {@link JsonView} classes
 * 
 * @see JsonView
 * @see Views
 * @see Views.ApplicationLogin
 * @see Views.ApplicationStudent
 */
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

    /**
     * Constructor for {@link PdfDocument}.
     * <p>Creates {@link PdfDocument} with given {@link String name} and byte[] pdfData.
     * 
     * @see PdfDocument
     */
    public PdfDocument(String name, byte[] pdfData) {
        this.name = name;
        this.pdfData = pdfData;
    }
}
