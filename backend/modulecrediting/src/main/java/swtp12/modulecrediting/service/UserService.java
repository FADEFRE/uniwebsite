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
import swtp12.modulecrediting.util.IncorrectKeyOnDecryptException;

/**
 * {@link UserService} is a {@link Service} and provides the 
 * and {@link #getUserProfile() getUserProfile} methods.
 * 
 * @see #getUserProfile()
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html"> Springboot @Service </a>
 * 
 * @author Frederik Kluge 
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
     * 
     * @return {@code UserSummary} of the authenticated {@code User}.
     * 
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}.
     * 
     * @see User
     * @see UserSummary
     */
    public UserSummary getUserProfile() {
        User user = identifyUser();
        return user.toUserSummary();
    }

    /**
     * This method gets the {@link UserSummary} of a current authenticated {@link User}. 
     * 
     * @return {@code UserSummary} of the authenticated {@code User}.
     * 
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}.
     * 
     * @see User
     * @see UserSummary
     */
    public UserSummary getUserProfileId() {
        User user = identifyUser();
        return user.toUserSummaryId();
    }

    /**
     * This method gets the {@link UserSummary} of a current authenticated {@link User}. 
     * 
     * @return {@code UserSummary} of the authenticated {@code User}.
     * 
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}.
     * 
     * @see User
     * @see UserSummary
     */
    public UserSummary getUserProfileName() {
        User user = identifyUser();
        return user.toUserSummaryName();
    }



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


    public String register(EditUserDTO registerRequest) {

        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null || registerRequest.getPasswordConfirm() == null || registerRequest.getRole() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username, password or role is null");

        Optional<User> userCandidate = userRepository.findByUsername(registerRequest.getUsername());
        if(userCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");

        Optional<Role> roleCandidate = roleRepository.findByRoleName(registerRequest.getRole());
        if (!roleCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role does not exist!"); 

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
        
        user.setRole(roleCandidate.get());
        userRepository.save(user);
        return "User registered successfully!";
    }


    public String deleteUser(EditUserDTO deleteRequest) {
        User user = identifyUser();

        if (deleteRequest.getId() == user.getUserId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete yourself");
        List<User> usersDB = userRepository.findAll();
        if(usersDB.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there are no users in the database");
        List<User> userAdmin = getAllUserWithRole("ROLE_ADMIN");
        if (userAdmin.size() == 1 && userAdmin.get(0).getUserId() == deleteRequest.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There has to be at least one admin user");

        Optional<User> userDeleteOptional = userRepository.findById(deleteRequest.getId());
        if(!userDeleteOptional.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user with this id found");

        userRepository.delete(userDeleteOptional.get());

        return "User deleted successfully!";
    }



    public String changeUsername(EditUserDTO changeRequest) throws IncorrectKeyOnDecryptException {
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

    public String changePassword(EditUserDTO changeRequest) {
        if(changeRequest.getPassword() == null || changeRequest.getPassword().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        if (changeRequest.getPassword().contains(" ") || changeRequest.getPasswordConfirm().contains(" ")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot contain whitespaces");

        User user = identifyUser();
        
        if (user.getUserId() != changeRequest.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id is not matching");
        User userDb = getUser(changeRequest.getId());

        if(!changeRequest.getPassword().equals(changeRequest.getPasswordConfirm())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not matching");

        userDb.setPassword(encoder.encode(changeRequest.getPassword()));
        userRepository.save(userDb);
        return "Password changed successfully";
    }

    public String changeRole(EditUserDTO changeRequest) {
        if(changeRequest.getRole() == null || changeRequest.getRole().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role cannot be empty");
        User userDb = getUser(changeRequest.getId());
        if (changeRequest.getId() == userDb.getUserId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't change your own Role");
        Optional<Role> roleOptional = roleRepository.findByRoleName(changeRequest.getRole());
        if (!roleOptional.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found in database");
        Role role = roleOptional.get();
        userDb.setRole(role);
        userRepository.save(userDb);
        return "Role changed successfully";
    }


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

    private User getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found in database");
        return userOptional.get();
    }

    public List<User> getAllUserWithRole(String roleName) {
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