package swtp12.modulecrediting.security.secService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import swtp12.modulecrediting.model.EnumUserRole;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.security.secOther.AuthenticationRequest;
import swtp12.modulecrediting.security.secOther.AuthenticationResponse;
import swtp12.modulecrediting.security.secOther.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    //TODO: Multiple User Roles
    //TODO: check for existing!!!
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(EnumUserRole.STUDY_OFFICE)
            .build();
        userRepository.save(user);
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwToken)
            .build();
    }
    


    public void writeUser(String username, String password, EnumUserRole role) {
        var user = User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .role(role)
            .build();
        userRepository.save(user);
    }
}
