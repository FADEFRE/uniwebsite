package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.LoginRequest;
import swtp12.modulecrediting.dto.LoginResponse;
import swtp12.modulecrediting.dto.LogoutResponse;
import swtp12.modulecrediting.dto.Token;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.util.CookieUtil;
import swtp12.modulecrediting.util.TokenProvider;

/**
 * This is a {@code Service} for {@code Authentication}
 * 
 * @see #login()
 * @see #refresh()
 * @see #logout()
 * @see #deleteRefreshCookie()
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Springboot Service</a>
 */
@Service
public class AuthService {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;


    /**
     * This method takes a {@link LoginRequest} and two {@code String} and checks if:
     * <p> - the {@link User}, given by the {@code username} field in the {@code loginRequest}, exists.
     * <p> - both {@code accessTokenString} and {@code refreshTokenString} are valid {@link Token}.
     * <p> Depending on these checks, the {@code User} gets logged in and the method creates new {@code accessToken} and {@code refreshToken} 
     * and puts them into {@code HttpHeaders} as {@code HttpCookies}.
     * @param loginRequest {@link LoginRequest} with username and password
     * @param accessTokenString {@code String} 
     * @param refreshTokenString {@code String} 
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}
     * @return {@code ResponseEntity} with the created {@code HttpHeaders} and a {@link LoginResponse} as body.
     * @see LoginResponse
     * @see LoginRequest
     * @see Token
     * @see User
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpCookie </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpHeaders </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring ResponseEntity </a>
     */
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessTokenString, String refreshTokenString) {
        String username = loginRequest.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with this username could not be found " + username));
        
        Boolean accessTokenValid = tokenProvider.validateToken(accessTokenString);
        Boolean refreshTokenValid = tokenProvider.validateToken(refreshTokenString);

        HttpHeaders responseHeaders = new HttpHeaders();
        Token newAccessToken;
        Token newRefreshToken;
        if (!accessTokenValid && !refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getUsername());
            newRefreshToken = tokenProvider.generateRefreshToken(user.getUsername());
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }
        if (!accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getUsername());
            addAccessTokenCookie(responseHeaders, newAccessToken);
        }
        if (accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getUsername());
            newRefreshToken = tokenProvider.generateRefreshToken(user.getUsername());
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    /**
     * This method takes two {@code String} and refreshes the {@link Token} of a current authenticated {@link User}. By
     * <p> - checking if the {@code refreshTokenString} is a valid {@code Token}.
     * <p> - trying to get the authenticated {@code User} from the {@code accessTokenString}.
     * <p> The method then creates a new {@code refreshToken} and puts it into {@code HttpHeaders} as {@code HttpCookies}.
     * @param accessTokenString {@code String} 
     * @param refreshTokenString {@code String} 
     * @throws IllegalArgumentException if {@code refreshTokenString} is an invalid {@link Token}.
     * @throws ResponseStatusException if getting the authenticated {@link User} from the {@code accessTokenString} is not possible. -> Code: 401
     * @return {@code ResponseEntity} with the created {@code HttpHeaders} and a {@link LoginResponse} as body.
     * @see LoginResponse
     * @see Token
     * @see User
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpCookie </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpHeaders </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring ResponseEntity </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException </a>
     */
    public ResponseEntity<LoginResponse> refresh(String accessTokenString, String refreshTokenString) {
        Boolean refreshTokenValid = tokenProvider.validateToken(refreshTokenString);
        if (!refreshTokenValid) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token is invalid!");

        String currentUserUsername = null;
        try { currentUserUsername = tokenProvider.getUsernameFromToken(accessTokenString); } 
        catch (Exception e) { 
            try { currentUserUsername = tokenProvider.getUsernameFromToken(refreshTokenString); } 
                catch (Exception e2) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No valid Tokens provided"); }
        }

        Token newAccessToken = tokenProvider.generateAccessToken(currentUserUsername);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, CookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    /**
     * This method deletes the {@link Token accessToken} by creating a new {@code accessToken} with a {@code MaxAge} of {@code 0}
     * and putting it into {@code HttpHeaders} as {@code HttpCookies}. 
     * This cookie is returned by the {@link CookieUtil#deleteAccessTokenCookie createAccessTokenCookie()} method in {@link CookieUtil}.
     * @return {@code ResponseEntity} with the created {@code HttpHeaders} and a {@link LogoutResponse} as body.
     * @see CookieUtil
     * @see CookieUtil#deleteAccessTokenCookie
     * @see LogoutResponse
     * @see Token
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpCookie </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpHeaders </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring ResponseEntity </a>
     */
    public static ResponseEntity<LogoutResponse> logout() {
        LogoutResponse logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.ERROR, "Error in userservice logout()");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, CookieUtil.deleteAccessTokenCookie().toString());

        logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.SUCCESS, "Successfully logged out");
        return ResponseEntity.ok().headers(responseHeaders).body(logoutResponse);
    }

    /**
     * This method deletes the {@link Token refreshToken} by creating a new {@code refreshToken} with a {@code MaxAge} of {@code 0}
     * and putting it into {@code HttpHeaders} as {@code HttpCookies}. 
     * This cookie is returned by the {@link CookieUtil#deleteRefreshTokenCookie deleteRefreshTokenCookie()} method in {@link CookieUtil}.
     * <p>This method is called when there is an error while decrypting the token, which happens most likely when the {@code refreshToken} 
     * was created on a diffrent version of the backend.
     * @return {@code ResponseEntity} with the created {@code HttpHeaders} and a {@link LogoutResponse} as body.
     * @see CookieUtil
     * @see CookieUtil#deleteRefreshTokenCookie
     * @see LogoutResponse
     * @see Token
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpCookie </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpHeaders </a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring ResponseEntity </a>
     */
    public static ResponseEntity<LogoutResponse> deleteRefreshCookie() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, CookieUtil.deleteRefreshTokenCookie().toString());

        LogoutResponse logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.SUCCESS, "Successfully deleted refreshToken");
        return ResponseEntity.ok().headers(responseHeaders).body(logoutResponse);
    }


    // ------- Private Methods -------

    /**
     * Adds the "accessToken" cookie by setting a new cookie to the given {@code httpHeaders}.
     * <p> This cookie is returned by the {@link CookieUtil#createAccessTokenCookie createAccessTokenCookie()} method in {@link CookieUtil}.
     * @see CookieUtil
     * @see CookieUtil#createAccessTokenCookie
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpHeaders </a>
     */
    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token accessToken) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, CookieUtil.createAccessTokenCookie(accessToken.getTokenValue(), accessToken.getDuration()).toString());
    }

    /**
     * Adds the "refreshToken" cookie by setting a new cookie to the given {@code httpHeaders}.
     * <p> This cookie is returned by the {@link CookieUtil#createRefreshTokenCookie() createRefreshTokenCookie()} method in {@link CookieUtil}.
     * @see CookieUtil
     * @see CookieUtil#createRefreshTokenCookie
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html">Spring HttpHeaders </a>
     */
    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token refreshToken) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, CookieUtil.createRefreshTokenCookie(refreshToken.getTokenValue(), refreshToken.getDuration()).toString());
    }

}
