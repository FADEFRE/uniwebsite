package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.EnumApplicationStatus;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{
    @EntityGraph(value = "graph.Application.modulesConnections", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Application> findById(Long id);

    Page<Application> findAllBy(Pageable pageable);

    Page<Application> findByFullStatus(EnumApplicationStatus status, Pageable pageable);
}
