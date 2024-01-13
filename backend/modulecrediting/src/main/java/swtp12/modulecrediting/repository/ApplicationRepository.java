package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String>{
    boolean existsById(String id);
}
