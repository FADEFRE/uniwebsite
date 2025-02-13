package swtp12.modulecrediting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import swtp12.modulecrediting.service.AuthService;
import swtp12.modulecrediting.util.SecurityCipher;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
        
    private final String accessTokenCookieName = "accessToken";
    private final String refreshTokenCookieName = "refreshToken";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @CookieValue(name = accessTokenCookieName, required = false) String accessToken,
            @CookieValue(name = refreshTokenCookieName, required = false) String refreshToken,
            @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername().toString(), loginRequest.getPassword().toString());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return authService.login(loginRequest, decryptedAccessToken, decryptedRefreshToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @CookieValue(name = accessTokenCookieName, required = false) String accessToken, 
            @CookieValue(name = refreshTokenCookieName, required = false) String refreshToken) {
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
                    return AuthService.logout();
                }
            }
        } 
        else logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.SUCCESS, "No cookies");
        return ResponseEntity.ok().body(logoutResponse);
    }

}
