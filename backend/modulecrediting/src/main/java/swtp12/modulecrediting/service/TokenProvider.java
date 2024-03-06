package swtp12.modulecrediting.service;


import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import swtp12.modulecrediting.dto.Token;


@Service
public class TokenProvider {

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpirationMsec}")
    private Long tokenExpirationMsec;

    @Value("${app.auth.refreshTokenExpirationMsec}")
    private Long refreshTokenExpirationMsec;


    public Token generateAccessToken(String subject) {
        Date now = new Date();
        Long duration = now.getTime() + tokenExpirationMsec;
        Date expiryDate = new Date(duration);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
        return new Token(Token.TokenType.ACCESS, token, tokenExpirationMsec, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }


    public Token generateRefreshToken(String subject) {
        Date now = new Date();
        Long duration = now.getTime() + refreshTokenExpirationMsec;
        Date expiryDate = new Date(duration);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
        return new Token(Token.TokenType.REFRESH, token, refreshTokenExpirationMsec, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }


    public String getUsernameFromToken(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }


    public LocalDateTime getExpiryDateFromToken(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }


    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) { return false; }
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Key getSignInKey() {
        byte[] keyByte = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
