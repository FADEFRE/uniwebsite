package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.ModuleLeipzig;

@Repository
public interface ModuleLeipzigRepository extends JpaRepository<ModuleLeipzig, String>{
    boolean existsByName(String name);
}
