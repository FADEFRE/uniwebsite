package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    
    Optional<Role> findByRoleName(String roleName);

}
