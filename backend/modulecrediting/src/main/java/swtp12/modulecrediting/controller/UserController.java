package swtp12.modulecrediting.controller;

import java.util.Optional;

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
@RequestMapping("/api/user")
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
    


    @GetMapping("/{id}/role")
    public ResponseEntity<String> getRole(@PathVariable String id) {
        if (id.equals("-1")) {
            return new ResponseEntity<>("Anonymous user", HttpStatus.OK); 
        }
        Long idLong = Long.parseLong(id);
        Optional<User> userCandidate = userRepository.findById(idLong);
        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            Role role = user.getRole();
            String roleName = role.getRoleName();
            return new ResponseEntity<>(roleName, HttpStatus.OK); 
        }
        return new ResponseEntity<>("User doesnt exists!", HttpStatus.BAD_REQUEST); 
    }

}
