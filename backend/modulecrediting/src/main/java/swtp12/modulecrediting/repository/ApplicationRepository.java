package swtp12.modulecrediting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.Application;

/**
 * This is the {@code JpaRepository} for {@link Application}
 * @see Application
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html">Spring Repository</a>
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html">Spring JpaRepository</a> 
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, String>{

    /**
     * Checks if a {@link Application} with the given id exists.
     * 
     * @return {@link Boolean true} if exists.
     * @see Application
     */
    boolean existsById(String id);
}
