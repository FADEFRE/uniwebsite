package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String>{
    @EntityGraph(value = "graph.Application.modulesConnections", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Application> findById(String id);

    boolean existsById(String id);
}
