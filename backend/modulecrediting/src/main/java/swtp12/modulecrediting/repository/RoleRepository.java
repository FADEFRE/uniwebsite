package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.Role;

/**
 * This is the {@code JpaRepository} for {@link Role}
 * @see Role
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html">Spring Repository</a>
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html">Spring JpaRepository</a> 
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    
    /**
     * Tries to get {@link Role} with given roleName and returns {@link Optional}
     * 
     * @return {@link Optional} that contains a {@link Role} or null.
     * @see Optional
     * @see Role
     */
    Optional<Role> findByRoleName(String roleName);

}
