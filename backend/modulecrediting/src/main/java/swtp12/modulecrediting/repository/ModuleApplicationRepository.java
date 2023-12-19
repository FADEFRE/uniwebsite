package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.ModuleApplication;
@Repository
public interface ModuleApplicationRepository extends JpaRepository<ModuleApplication, Long>{
    
}
