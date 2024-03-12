package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.CourseLeipzig;

import java.util.Optional;

/**
 * This is the {@code JpaRepository} for {@link CourseLeipzig}
 * @see CourseLeipzig
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html">Spring Repository</a>
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html">Spring JpaRepository</a> 
 */
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

