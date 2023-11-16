package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import swtp12.modulecrediting.model.ModuleApplication;

public interface ModuleApplicationRepository extends JpaRepository<ModuleApplication, Long>{
    
}
