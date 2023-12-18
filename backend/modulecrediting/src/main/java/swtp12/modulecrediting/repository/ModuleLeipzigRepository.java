package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.ModuleLeipzig;

import java.util.Optional;

@Repository
public interface ModuleLeipzigRepository extends JpaRepository<ModuleLeipzig, String>{
    Optional<ModuleLeipzig> findByModuleName(String moduleName);

    boolean existsByModuleName(String moduleName);
}
