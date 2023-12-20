package swtp12.modulecrediting.security.secController;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import swtp12.modulecrediting.security.secOther.AuthenticationRequest;
import swtp12.modulecrediting.security.secOther.AuthenticationResponse;
import swtp12.modulecrediting.security.secOther.RegisterRequest;
import swtp12.modulecrediting.security.secService.AuthenticationService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticaionController {

    private final AuthenticationService service;

    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    
}
