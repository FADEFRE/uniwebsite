package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.CourseLeipzig;

@Repository
public interface CourseLeipzigRepository extends JpaRepository<CourseLeipzig, String>{

    boolean existsByName(String name);
}
