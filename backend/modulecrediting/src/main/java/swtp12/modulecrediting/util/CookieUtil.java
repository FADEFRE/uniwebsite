package swtp12.modulecrediting.util;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * This {@link Component} includes functions to create and delete {@link HttpCookie HttpCookies}.
 */
@Component
public class CookieUtil {

    private static final String accessTokenCookieName = "accessToken";
    private static final String refreshTokenCookieName = "refreshToken";

    /**
     * This function creates {@code AccessTokenCookie} with given {@link String token} and {@link Long duration}.
     * @param token {@code String }
     * @param duration {@code Long}
     * @return AccessToken in {@code HttpCookie} 
     */
    public static HttpCookie createAccessTokenCookie(String token, Long duration) {
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

    /**
     * This function creates {@link HttpCookie RefreshTokenCookie} with given {@link String token} and {@link Long duration}.
     * @param token {@code String }
     * @param duration {@code Long}
     * @return RefreshToken in {@code HttpCookie} 
     */
    public static HttpCookie createRefreshTokenCookie(String token, Long duration) {
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

    /**
     * This function deletes {@link HttpCookie AccessTokenCookie} by creating a new one with 'maxAge' of 0.
     * @return {@code AccessCookie} with maxAge of 0
     */
    public static HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(accessTokenCookieName, null)
                .maxAge(0)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

    /**
     * This function deletes {@link HttpCookie RefreshTokenCookie} by creating a new one with 'maxAge' of 0.
     * @return {@code RefreshCookie} with maxAge of 0
     */
    public static HttpCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenCookieName, null)
                .maxAge(0)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

}