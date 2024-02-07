package swtp12.modulecrediting.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import swtp12.modulecrediting.dto.LoginRequest;
import swtp12.modulecrediting.dto.LoginResponse;
import swtp12.modulecrediting.dto.LogoutResponse;
import swtp12.modulecrediting.dto.EditUserDTO;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.RoleRepository;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.service.AuthService;
import swtp12.modulecrediting.util.IncorrectKeyOnDecryptException;
import swtp12.modulecrediting.util.SecurityCipher;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Value("${app.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${app.auth.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @Valid @RequestBody LoginRequest loginRequest
    ) throws IncorrectKeyOnDecryptException {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername().toString(), loginRequest.getPassword().toString());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return authService.login(loginRequest, decryptedAccessToken, decryptedRefreshToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @CookieValue(name = "accessToken", required = false) String accessToken, 
            @CookieValue(name = "refreshToken", required = false) String refreshToken) throws IncorrectKeyOnDecryptException {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return authService.refresh(decryptedAccessToken, decryptedRefreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
        LogoutResponse logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.ERROR, "Error in api/auth/logout");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(accessTokenCookieName)) {
                    return authService.logout();
                }
            }
        } 
        else logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.SUCCESS, "No cookies");
        return ResponseEntity.ok().body(logoutResponse);
    }
    

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> registerUser(@Valid @RequestBody EditUserDTO registerRequest) {
        Optional<User> userCandidate = userRepository.findByUsername(registerRequest.getUsername());

        if (!userCandidate.isPresent()) {
            User user = new User(
                registerRequest.getUsername(),
                encoder.encode(registerRequest.getPassword()),
                true
            );
            Optional<Role> roleCandidate = roleRepository.findByRoleName(registerRequest.getRole());
            if (roleCandidate.isPresent()) {
                if(user.getRole() == null) {
                    Role role = new Role();
                    user.setRole(role);
                }
                else {
                    user.setRole(roleCandidate.get());
                }
                userRepository.save(user);
                return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
    }
}
