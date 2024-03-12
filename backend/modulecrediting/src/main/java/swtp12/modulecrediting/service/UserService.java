package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.CustomUserDetails;
import swtp12.modulecrediting.dto.EditUserDTO;
import swtp12.modulecrediting.dto.UserSummary;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.RoleRepository;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.util.LogUtil;

/**
 * This is a {@code Service} for {@link User}
 * @author Frederik Kluge
 * @see #getUserProfile
 * @see #getUserProfileId
 * @see #getUserProfileName
 * @see #getAllUsers
 * @see #register
 * @see #deleteUser
 * @see #changeUsername
 * @see #changePassword
 * @see #changeRole
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Springboot Service</a>
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;


    /**
     * This method gets the {@link UserSummary} of a current authenticated {@link User}. 
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}.
     * @return {@code UserSummary} of the authenticated {@code User}.
     * @see User
     * @see UserSummary
     */
    public UserSummary getUserProfile() {
        User user = identifyUser();
        return user.toUserSummary();
    }

    /**
     * This method gets the {@link UserSummary} of a current authenticated {@link User}. 
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}.
     * @return {@code UserSummary} of the authenticated {@code User}.
     * @see User
     * @see UserSummary
     */
    public UserSummary getUserProfileId() {
        User user = identifyUser();
        return user.toUserSummaryId();
    }

    /**
     * This method gets the {@link UserSummary} of a current authenticated {@link User}.
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}.
     * @return {@code UserSummary} of the authenticated {@code User}.
     * @see User
     * @see UserSummary
     */
    public UserSummary getUserProfileName() {
        User user = identifyUser();
        return user.toUserSummaryName();
    }

    /**
     * This method gets a {@code List} of {@link UserSummary UserSummaries} of all {@link User Users} saved in the database. 
     * @throws ResponseStatusException with {@link HttpStatus NOT_FOUND} if no {@link User Users} are saved in the database.
     * @return {@code List} of {@link UserSummary UserSummaries}
     * @see User
     * @see UserSummary
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public List<UserSummary> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserSummary> userSummaries = new ArrayList<>();

        if(users.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there are no users in the database");

        for (User user : users) {
            UserSummary userSummary = new UserSummary();
            userSummary.setUsername(user.getUsername());
            userSummary.setUserId(user.getUserId());
            userSummary.setRole(user.getRole().getRoleName());
            userSummaries.add(userSummary);
        }
        return userSummaries;
    }

    /**
     * This method creates a new {@link User}.
     * @param registerRequest {@link EditUserDTO} with {@code username}, {@code password}, {@code passwordConfirm} and {@code role}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if any of the needed {@link EditUserDTO} fields are {@code null} or {@code blank}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code role} does not exist.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code username} or {@code password(Confirm)} include {@code whitespaces}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code password} and {@code passwordConfirm} are {@code not equal}.
     * @throws ResponseStatusException with {@link HttpStatus CONFLICT} if a {@link User} with the given username already {@code exists}.
     * @return {@code String: Success message}
     * @see User
     * @see EditUserDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String register(EditUserDTO registerRequest) {

        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null || registerRequest.getPasswordConfirm() == null || registerRequest.getRole() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username, password or role is null");

        Optional<User> userCandidate = userRepository.findByUsername(registerRequest.getUsername());
        if(userCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");

        Role role = roleRepository.findByRoleName(registerRequest.getRole()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role does not exist!"));

        if (registerRequest.getUsername().contains(" ") || registerRequest.getPassword().contains(" ") || registerRequest.getPasswordConfirm().contains(" ") )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password cannot include whitespaces"); 

        if (registerRequest.getUsername().isBlank() || registerRequest.getPassword().isBlank() || registerRequest.getPasswordConfirm().isBlank() )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password cannot be empty"); 

        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not equal"); 

        User user = new User(
            registerRequest.getUsername(),
            encoder.encode(registerRequest.getPassword()),
            true
        );
        
        user.setRole(role);
        userRepository.save(user);
        LogUtil.printUserLog(LogUtil.UserType.CREATED, user.getUsername(), user.getRole().getRoleName(), null, null);
        return "User registered successfully!";
    }

    /**
     * This method deletes a {@link User}.
     * @param deleteRequest {@link EditUserDTO} with {@code id}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if no {@link User Users} are saved in the database.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code id} is {@code null}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link User} making the request is the same as the one to be deleted.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the to be deleted {@link User} is the only {@link Role Admin}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link User} with the given {@code userId} is not in the database.
     * @throws IllegalArgumentException if no {@link User} is logged in.
     * @return {@code String: Success message}
     * @see User
     * @see Role
     * @see EditUserDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String deleteUser(EditUserDTO deleteRequest) {
        if(deleteRequest.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id cannot be null");
        User user = identifyUser();

        if (deleteRequest.getId() == user.getUserId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete yourself");
        List<User> usersDB = userRepository.findAll();
        if(usersDB.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "there are no users in the database");
        List<User> userAdmin = getAllUserWithRole("ROLE_ADMIN");
        if (userAdmin.size() == 1 && userAdmin.get(0).getUserId() == deleteRequest.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There has to be at least one admin user");

        User userDelete = userRepository.findById(deleteRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user with this id found"));

        
        userRepository.delete(userDelete);
        LogUtil.printUserLog(LogUtil.UserType.DELETED, userDelete.getUsername(), userDelete.getRole().getRoleName(), null, null);
        return "User deleted successfully!";
    }

    /**
     * This method changes the {@code username} of a {@link User}.
     * @param changeRequest {@link EditUserDTO} with {@code id} and new {@code username}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code id} is {@code null}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link User} making the request is not the same as the one to be changed.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the new {@code username} includes {@code whitespaces}, is {@code null} or {@code blank}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link User} with the given {@code userId} is not in the database.
     * @throws ResponseStatusException with {@link HttpStatus CONFLICT} if a {@link User} with the given username already {@code exists}.
     * @throws IllegalArgumentException if no {@link User} is logged in.
     * @return {@code String: Success message}
     * @see User
     * @see EditUserDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String changeUsername(EditUserDTO changeRequest) {
        if(changeRequest.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id cannot be null");
        if(changeRequest.getUsername() == null || changeRequest.getUsername().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        if (changeRequest.getUsername().contains(" ")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot contain whitespaces"); 

        User user = identifyUser();

        Optional<User> userCandidate = userRepository.findByUsername(changeRequest.getUsername());
        if(userCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");

        if (user.getUserId() != changeRequest.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id is not matching");
        User userDb = getUser(changeRequest.getId());
        userDb.setUsername(changeRequest.getUsername());
        userRepository.save(userDb);
        return "Username changed successfully";
    }

    /**
     * This method changes the {@code password} of a {@link User}.
     * @param changeRequest {@link EditUserDTO} with {@code id} and new {@code password} and {@code passwordConfirm}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code id} is {@code null}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link User} making the request is not the same as the one to be changed.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the new {@code password} or {@code passwordConfirm} include {@code whitespaces}, is {@code null} or {@code blank}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link User} with the given {@code userId} is not in the database.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code password} and {@code passwordConfirm} are {@code not equal}.
     * @throws IllegalArgumentException if no {@link User} is logged in.
     * @return {@code String: Success message}
     * @see User
     * @see EditUserDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String changePassword(EditUserDTO changeRequest) {
        if(changeRequest.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id cannot be null");
        if(changeRequest.getPassword() == null || changeRequest.getPassword().isBlank() || changeRequest.getPasswordConfirm() == null || changeRequest.getPasswordConfirm().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        if (changeRequest.getPassword().contains(" ") || changeRequest.getPasswordConfirm().contains(" ")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot contain whitespaces");

        User user = identifyUser();
        
        if (user.getUserId() != changeRequest.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id is not matching");
        User userDb = getUser(changeRequest.getId());

        if(!changeRequest.getPassword().equals(changeRequest.getPasswordConfirm())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not matching");

        userDb.setPassword(encoder.encode(changeRequest.getPassword()));
        userRepository.save(userDb);
        return "Password changed successfully";
    }

    /**
     * This method changes the {@code role} of a {@link User}.
     * @param changeRequest {@link EditUserDTO} with {@code id} and new {@code password} and {@code passwordConfirm}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@code id} is {@code null}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the new {@code role} is {@code null} or {@code blank}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link User} making the request is the same as the one to be changed.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the given {@link Role} is not in the database.
     * @throws IllegalArgumentException if no {@link User} is logged in.
     * @return {@code String: Success message}
     * @see User
     * @see Role
     * @see EditUserDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String changeRole(EditUserDTO changeRequest) {
        if(changeRequest.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id cannot be null");
        if(changeRequest.getRole() == null || changeRequest.getRole().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role cannot be empty");
        User user = identifyUser();
        if (changeRequest.getId() == user.getUserId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't change your own Role");
        Role role = roleRepository.findByRoleName(changeRequest.getRole()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found in database"));
        
        User userDb = getUser(changeRequest.getId());
        Role oldRole = userDb.getRole();
        userDb.setRole(role);
        userRepository.save(userDb);
        LogUtil.printUserLog(LogUtil.UserType.ROLE_CHANGED, userDb.getUsername(), oldRole.getRoleName(), null, userDb.getRole().getRoleName());
        return "Role changed successfully";
    }


    // ------- Private Methods -------

    /**
     * This method gets the current logged in {@link User}.
     * @throws IllegalArgumentException if no {@link User} is logged in.
     * @return current logged in {@link User}.
     * @see User
     */
    private User identifyUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        Object obj = authentication.getPrincipal();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        if (customUserDetails.getClass().isInstance(obj)) {
            customUserDetails = (CustomUserDetails) obj;
            String username = customUserDetails.getUsername();
            if (!username.equals("anonymousUser")) {
                user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with this username could not be found " + username));
            }
        }
        return user;
    }

    /**
     * This method gets a {@link User} from the database by {@code id}.
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if no {@link User} with the given {@code id} is found.
     * @return {@link User} with given {@code id}.
     * @see User
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    private User getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found in database");
        return userOptional.get();
    }

    /**
     * This method gets all {@link User Users} from the database with the given {@code rolename}.
     * @return List of {@link User Users} with given {@code rolename}.
     * @see User
     */
    private List<User> getAllUserWithRole(String roleName) {
        List<User> userWithRole = new ArrayList<>();
        List<User> usersDB = userRepository.findAll();
        for (User userDB : usersDB) {
            if (userDB.getRole().getRoleName().equals(roleName)) {
                userWithRole.add(userDB);
            }
        }
        return userWithRole;
    }

}
