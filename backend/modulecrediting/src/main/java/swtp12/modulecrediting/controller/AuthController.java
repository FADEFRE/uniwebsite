package swtp12.modulecrediting.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import swtp12.modulecrediting.dto.RegisterRequest;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.RoleRepository;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.service.UserService;
import swtp12.modulecrediting.util.SecurityCipher;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    
    @Value("${app.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${app.auth.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

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
    ) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername().toString(), loginRequest.getPassword().toString());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return userService.login(loginRequest, decryptedAccessToken, decryptedRefreshToken);
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = "accessToken", required = false) String accessToken, @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return userService.refresh(decryptedAccessToken, decryptedRefreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {      
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(accessTokenCookieName)) {
                    HttpCookie httpCookie = ResponseCookie.from(accessTokenCookieName, null).maxAge(0).httpOnly(true).path("/").build();
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());
                    LogoutResponse logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.SUCCESS, "Successfully logged out");
                    return ResponseEntity.ok().headers(responseHeaders).body(logoutResponse);
                }
            }
        }
        LogoutResponse logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.SUCCESS, "No cookies");
        return ResponseEntity.ok().body(logoutResponse);
    }
    

    @PostMapping(value = "/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        Optional<User> userCandidate = userRepository.findByUsername(registerRequest.getUsername());

        if (!userCandidate.isPresent()) {
            User user = new User(
                registerRequest.getUsername(),
                encoder.encode(registerRequest.getPassword()),
                true
            );
            Optional<Role> roleCandidate = roleRepository.findByRoleName(registerRequest.getRole().getRoleName());
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
