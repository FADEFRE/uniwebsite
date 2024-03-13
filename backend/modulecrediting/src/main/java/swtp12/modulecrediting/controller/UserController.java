package swtp12.modulecrediting.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.dto.EditUserDTO;
import swtp12.modulecrediting.dto.UserSummary;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.service.UserService;



@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    /**
     * Get {@link GetMapping /api/user/me} 
     * <p> Returns {@link UserSummary} of the currently logged in {@link User}
     * 
     * @return {@link UserSummary}
     * @see GetMapping
     * @see User
     * @see UserSummary
     */
    @GetMapping("/me")
    public ResponseEntity<UserSummary> getMeUser() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    /**
     * Get {@link GetMapping /api/user/me/id} 
     * <p> Returns {@link UserSummary} of the currently logged in {@link User}
     * 
     * @return {@link UserSummary}
     * @see GetMapping
     * @see User
     * @see UserSummary
     */
    @GetMapping("/me/id")
    public ResponseEntity<UserSummary> getMeUserId() {
        return ResponseEntity.ok(userService.getUserProfileId());
    }

        /**
     * Get {@link GetMapping /api/user/me/name} 
     * <p> Returns {@link UserSummary} of the currently logged in {@link User}
     * 
     * @return {@link UserSummary}
     * @see GetMapping
     * @see User
     * @see UserSummary
     */
    @GetMapping("/me/name")
    public ResponseEntity<UserSummary> getMeUserName() {
        return ResponseEntity.ok(userService.getUserProfileName());
    }



    /**
     * Get {@link GetMapping /api/user/role} 
     * <p> Returns {@link Role} name of the currently logged in {@link User} or {@code User} if current user is not logged in
     * 
     * @return Role name
     * @see GetMapping
     * @see PreAuthorize
     * @see Role
     * @see User
     */
    @GetMapping("/role")
    public ResponseEntity<String> getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userCandidate = userRepository.findByUsername(auth.getName());
        if (userCandidate.isPresent()) {
            String roleName = userCandidate.get().getRole().getRoleName();
            return new ResponseEntity<>(roleName, HttpStatus.OK); 
        }
        return new ResponseEntity<>("User", HttpStatus.OK); 
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserSummary>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> registerUser(@ModelAttribute EditUserDTO registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }


    @PutMapping("/change/username")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeUsername(@ModelAttribute EditUserDTO changeRequest) {
        return ResponseEntity.ok(userService.changeUsername(changeRequest));
    }

    @PutMapping("/change/password")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changePassword(@ModelAttribute EditUserDTO changeRequest) {
        return ResponseEntity.ok(userService.changePassword(changeRequest));
    }

    @PutMapping("/change/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeRole(@ModelAttribute EditUserDTO changeRequest) {
        return ResponseEntity.ok(userService.changeRole(changeRequest));
    }
    
    
    @PutMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@ModelAttribute EditUserDTO deleteRequest) {
        return ResponseEntity.ok(userService.deleteUser(deleteRequest));
    }
}
