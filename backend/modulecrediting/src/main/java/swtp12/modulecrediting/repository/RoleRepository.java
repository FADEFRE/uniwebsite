package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.Role;

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
