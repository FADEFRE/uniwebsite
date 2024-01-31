package swtp12.modulecrediting.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/me")
    public ResponseEntity<UserSummary> getMeUser() {
        return ResponseEntity.ok(userService.getUserProfile());
    }
    


    @GetMapping("/role")
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<User> userCandidate = userRepository.findByUsername(name);
        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            Role role = user.getRole();
            String roleName = role.getRoleName();
            return new ResponseEntity<>(roleName, HttpStatus.OK); 
        }
        return new ResponseEntity<>("User doesnt exists!", HttpStatus.NOT_FOUND); 
    }

}
