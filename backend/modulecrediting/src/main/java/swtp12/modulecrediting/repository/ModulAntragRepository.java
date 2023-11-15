package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import swtp12.modulecrediting.model.ModulAntrag;

public interface ModulAntragRepository extends JpaRepository<ModulAntrag, Long>{
    
}
