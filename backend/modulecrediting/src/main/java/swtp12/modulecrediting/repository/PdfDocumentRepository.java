package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.PdfDocument;

@Repository
public interface PdfDocumentRepository extends JpaRepository<PdfDocument, Long>{

}
