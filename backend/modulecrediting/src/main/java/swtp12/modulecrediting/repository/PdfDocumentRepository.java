package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.PdfDocument;

/**
 * This is the {@code JpaRepository} for {@link PdfDocument}
 * @see PdfDocument
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html">Spring Repository</a>
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html">Spring JpaRepository</a> 
 */
@Repository
public interface PdfDocumentRepository extends JpaRepository<PdfDocument, Long>{

}
