package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import swtp12.modulecrediting.model.Antrag;

public interface AntragRepository extends JpaRepository<Antrag, Long>{
    
}
