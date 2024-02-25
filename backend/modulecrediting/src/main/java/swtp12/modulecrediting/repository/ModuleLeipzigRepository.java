package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.ModuleLeipzig;

import java.util.Optional;

@Repository
public interface ModuleLeipzigRepository extends JpaRepository<ModuleLeipzig, Long> {

    /**
     * Checks if a {@link ModuleLeipzig} with the given name and code exists.
     * 
     * @return {@link Boolean true} if exists.
     * @see ModuleLeipzig
     */
    boolean existsByNameAndCode(String name, String code);


    /**
     * Tries to get {@link ModuleLeipzig} with given name and returns {@link Optional}
     * 
     * @return {@link Optional} that contains a {@link ModuleLeipzig} or null.
     * @see Optional
     * @see ModuleLeipzig
     */
    Optional<ModuleLeipzig> findByName(String name);

    /**
     * Tries to get {@link ModuleLeipzig} with given code and returns {@link Optional}
     * 
     * @return {@link Optional} that contains a {@link ModuleLeipzig} or null.
     * @see Optional
     * @see ModuleLeipzig
     */
    Optional<ModuleLeipzig> findByCode(String code);
}
