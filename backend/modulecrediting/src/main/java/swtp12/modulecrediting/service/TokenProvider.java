package swtp12.modulecrediting.service;

import java.time.LocalDateTime;

import swtp12.modulecrediting.dto.Token;

public interface TokenProvider {
    Token generateAccessToken(String subject);

    Token generateRefreshToken(String subject);

    String getUsernameFromToken(String token);

    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateToken(String token);
}