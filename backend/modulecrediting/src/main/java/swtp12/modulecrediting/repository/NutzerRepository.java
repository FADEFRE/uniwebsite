package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import swtp12.modulecrediting.model.Nutzer;

public interface NutzerRepository extends JpaRepository<Nutzer, Long>{
    
}
