package swtp12.modulecrediting.service;

import org.springframework.http.ResponseEntity;

import swtp12.modulecrediting.dto.LoginRequest;
import swtp12.modulecrediting.dto.LoginResponse;
import swtp12.modulecrediting.dto.LogoutResponse;
import swtp12.modulecrediting.dto.UserSummary;

public interface UserService {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken);

    public ResponseEntity<LogoutResponse> logout();

    UserSummary getUserProfile();
}
