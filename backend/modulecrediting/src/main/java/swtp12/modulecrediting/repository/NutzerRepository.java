package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.Nutzer;
@Repository
public interface NutzerRepository extends JpaRepository<Nutzer, Long>{
    
}
