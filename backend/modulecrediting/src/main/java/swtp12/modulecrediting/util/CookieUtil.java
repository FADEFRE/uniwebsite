package swtp12.modulecrediting.util;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private final String accessTokenCookieName = "accessToken";
    private final String refreshTokenCookieName = "refreshToken";

    public HttpCookie createAccessTokenCookie(String token, Long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);
        duration = duration / 1000;
        return ResponseCookie.from(accessTokenCookieName, encryptedToken)
                .maxAge(duration)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

    public HttpCookie createRefreshTokenCookie(String token, Long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);
        duration = duration / 1000;
        return ResponseCookie.from(refreshTokenCookieName, encryptedToken)
                .maxAge(duration)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(accessTokenCookieName, null)
                .maxAge(0)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

    public HttpCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenCookieName, null)
                .maxAge(0)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

}