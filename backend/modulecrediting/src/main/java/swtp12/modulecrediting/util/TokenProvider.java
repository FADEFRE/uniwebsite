package swtp12.modulecrediting.util;


import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    @Value("#{new Long('${app.auth.tokenExpirationMsec}')}")
    private Long tokenExpirationMsec;
    @Value("#{new Long('${app.auth.refreshTokenExpirationMsec}')}")
    private Long refreshTokenExpirationMsec;
    @Value("#{new Boolean('${app.auth.expireAtMidnight}')}")
    private Boolean expireAtMidnight;


    public Token generateAccessToken(String subject) {
        Date now = new Date();
        Long duration = now.getTime() + tokenExpirationMsec;
        Date expiryDate = new Date(duration);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        return new Token(Token.TokenType.ACCESS, token, tokenExpirationMsec, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }


    public Token generateRefreshToken(String subject) {
        Date now = new Date();
        Date expiryDate = getRefreshExpiryDate(now, refreshTokenExpirationMsec, expireAtMidnight);
        Long newExpirationMsec = expiryDate.getTime() - now.getTime();
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        return new Token(Token.TokenType.REFRESH, token, newExpirationMsec, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }


    public String getUsernameFromToken(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }


    public LocalDateTime getExpiryDateFromToken(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }


    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) { return false; }
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
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

    private Key getSigningKey() {
        byte[] keyByte = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyByte);
    }

    private Date getRefreshExpiryDate(Date now, Long refreshTokenExpirationMsec, Boolean expireAtMidnight) {
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMsec);
        if (!expireAtMidnight) {
            return expiryDate;
        }
        else {
            Calendar cal = new GregorianCalendar();
            cal.setTime(expiryDate);
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
            cal.set(Calendar.HOUR_OF_DAY, 1); //this 1 hour offset is here to account for the wintertime //TODO: maybe make this dynamic
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date expiryDateMidnight = cal.getTime();
            return expiryDateMidnight;
        }
    }
}
