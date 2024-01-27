package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.CourseLeipzig;

import java.util.Optional;

@Repository
public interface CourseLeipzigRepository extends JpaRepository<CourseLeipzig, Long> {
    boolean existsByName(String name);
    Optional<CourseLeipzig> findByName(String name);
}

