package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{


    /**
     * Tries to get {@link User} with given username and returns {@link Optional}
     * 
     * @return {@link Optional} that contains a {@link User} or null.
     * @see Optional
     * @see User
     */
    Optional<User> findByUsername(String username);


    /**
     * Checks if a {@link User} with the given username exists.
     * 
     * @return {@link Boolean true} if exists.
     * @see User
     */
    Boolean existsByUsername(String username);
}
