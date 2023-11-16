package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import swtp12.modulecrediting.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long>{
    
}
