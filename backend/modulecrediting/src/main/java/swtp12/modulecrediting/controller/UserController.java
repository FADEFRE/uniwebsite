package swtp12.modulecrediting.controller;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.dto.UserSummary;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.service.UserService;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserSummary> getMeUser() {
        return ResponseEntity.ok(userService.getUserProfile());
    }
    


    @GetMapping("/{username}/highRole")
    public ResponseEntity<String> getHighestRole(@PathVariable String username) {
        Optional<User> userCandidate = userRepository.findByUsername(username);
        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            Set<Role> roles = user.getRoles();
            String highestRole = "";
            for (Role role : roles) {
                role.getRoleName();
                if (role.getRoleName().equals("admin")) { highestRole = "admin"; }
                else if (role.getRoleName().equals("chairman")) { highestRole = "chairman"; }
                else { highestRole = "studyoffice"; }
            }
            return new ResponseEntity<>(highestRole, HttpStatus.OK); 
        }
        return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST); 
    }

}
