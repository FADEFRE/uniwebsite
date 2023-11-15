package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import swtp12.modulecrediting.model.Pdf;

public interface PdfRepository extends JpaRepository<Pdf, Long>{
    
}
