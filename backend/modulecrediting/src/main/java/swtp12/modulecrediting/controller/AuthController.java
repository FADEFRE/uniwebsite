package swtp12.modulecrediting.controller;

import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
    
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }
    
}
