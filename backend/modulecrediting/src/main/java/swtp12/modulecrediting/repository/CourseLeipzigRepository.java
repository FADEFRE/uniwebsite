package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.CourseLeipzig;

import java.util.Optional;

@Repository
public interface CourseLeipzigRepository extends JpaRepository<CourseLeipzig, Long> {

    /**
     * Checks if a {@link CourseLeipzig} with the given name exists.
     * 
     * @return {@link Boolean true} if exists.
     * @see CourseLeipzig
     */
    boolean existsByName(String name);

    /**
     * Tries to get {@link CourseLeipzig} with given name and returns {@link Optional}
     * 
     * @return {@link Optional} that contains a {@link CourseLeipzig} or null.
     * @see Optional
     * @see CourseLeipzig
     */
    Optional<CourseLeipzig> findByName(String name);
}

