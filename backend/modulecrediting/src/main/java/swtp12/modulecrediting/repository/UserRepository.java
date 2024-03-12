package swtp12.modulecrediting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import swtp12.modulecrediting.model.User;

/**
 * This is the {@code JpaRepository} for {@link User}
 * @see User
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html">Spring Repository</a>
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html">Spring JpaRepository</a> 
 */
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
