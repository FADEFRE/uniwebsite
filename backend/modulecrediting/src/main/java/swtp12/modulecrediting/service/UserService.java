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
        Optional<User> userCandidate = userRepository.findByUsername(registerRequest.getUsername());
        if(userCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");

        Optional<Role> roleCandidate = roleRepository.findByRoleName(registerRequest.getRole());
        if (!roleCandidate.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role does not exist!"); 

        User user = new User(
            registerRequest.getUsername(),
            encoder.encode(registerRequest.getPassword()),
            true
        );
        
        user.setRole(roleCandidate.get());
        userRepository.save(user);
        return "User registered successfully!";
    }


    public String deleteUser() {
        throw new Error("not implemented yet");
        //check for cant delete self
    }



    public String changeUsername(EditUserDTO changeRequest) {
        if(changeRequest.getUsername() == null || changeRequest.getUsername().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
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
        User user = identifyUser();
        if (user.getUserId() != changeRequest.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id is not matching");
        User userDb = getUser(changeRequest.getId());

        System.out.println("pw " + changeRequest.getPassword());
        System.out.println("pwC " + changeRequest.getPasswordConfirm());

        if(changeRequest.getPassword().equals(changeRequest.getPasswordConfirm())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not matching");



        userDb.setPassword(encoder.encode(changeRequest.getPassword()));
        userRepository.save(userDb);
        return "Password changed successfully";
    }

    public String changeRole(EditUserDTO changeRequest) {
        if(changeRequest.getRole() == null || changeRequest.getRole().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role cannot be empty");
        User userDb = getUser(changeRequest.getId());
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

}