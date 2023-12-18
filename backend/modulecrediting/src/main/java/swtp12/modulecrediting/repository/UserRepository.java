package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swtp12.modulecrediting.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    Optional<User> findByUsername(String username);
}
