package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.CustomUserDetails;
import swtp12.modulecrediting.dto.LoginRequest;
import swtp12.modulecrediting.dto.LoginResponse;
import swtp12.modulecrediting.dto.LogoutResponse;
import swtp12.modulecrediting.dto.Token;
import swtp12.modulecrediting.dto.UserSummary;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;
import swtp12.modulecrediting.util.CookieUtil;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CookieUtil cookieUtil;

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken) {
        String username = loginRequest.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with this username could not be found " + username));
        
        Boolean accessTokenValid = tokenProvider.validateToken(accessToken);
        Boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);

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
     * Returns a {@link ResponseEntity} containing a {@link LoginResponse} and adds a new accessToken cookie.
     * 
     * @return The method is returning a {@link ResponseEntity} object with a body of type {@link LoginResponse}.
     * @see LoginResponse
     * @see ResponseEntity
     * @see CookieUtil
     * @see CookieUtil#createAccessTokenCookie()
     */
    public ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken) {
        Boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);
        if (!refreshTokenValid) throw new IllegalArgumentException("Refresh Token is invalid!");

        String currentUserUsername = null;
        try { currentUserUsername = tokenProvider.getUsernameFromToken(accessToken); } 
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No accessToken provided, you are most likely not actively logged in"); }

        Token newAccessToken = tokenProvider.generateAccessToken(currentUserUsername);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }
    /**
     * Returns a {@link ResponseEntity} containing a {@link LogoutResponse} with a success or error message, and deletes the accessToken cookie.
     * 
     * @return The method is returning a {@link ResponseEntity} object with a body of type {@link LogoutResponse}.
     * @see LogoutResponse
     * @see ResponseEntity
     * @see #deleteAccessTokenCookie()
     */
    public ResponseEntity<LogoutResponse> logout() {
        HttpHeaders responseHeaders = new HttpHeaders();
        LogoutResponse logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.ERROR, "Error in userservice logout()");
        deleteAccessTokenCookie(responseHeaders);
        logoutResponse = new LogoutResponse(LogoutResponse.SuccessFailure.SUCCESS, "Successfully logged out");
        return ResponseEntity.ok().headers(responseHeaders).body(logoutResponse);
    }

    /**
     * Retrieves the user profile based on the currently authenticated {@link User}.
     * 
     * @return The method is returning a {@link UserSummary} object of the current {@link User}.
     * @see User
     * @see UserSummary
     */
    public UserSummary getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        Object obj = authentication.getPrincipal();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        if (customUserDetails.getClass().isInstance(obj)) {
            customUserDetails = (CustomUserDetails) obj;
            String username = customUserDetails.getUsername();
            if (!username.equals("anonymousUser")) {
                user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with this username could not be found " + username));
            }
        }
        return user.toUserSummary();
    }

    /**
     * Adds the "accessToken" cookie by {@link HttpHeaders#SET_COOKIE SET_COOKIE} a new cookie to the {@link HttpHeaders}.
     * <p> This cookie is returned by the {@link CookieUtil#createAccessTokenCookie() createAccessTokenCookie()} method in {@link CookieUtil}.
     * 
     * @see CookieUtil
     * @see CookieUtil#createAccessTokenCookie()
     * @see HttpHeaders
     */
    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    /**
     * Adds the "refreshToken" cookie by {@link HttpHeaders#SET_COOKIE SET_COOKIE} a new cookie to the {@link HttpHeaders}.
     * <p> This cookie is returned by the {@link CookieUtil#createRefreshTokenCookie() createRefreshTokenCookie()} method in {@link CookieUtil}.
     * 
     * @see CookieUtil
     * @see CookieUtil#createRefreshTokenCookie()
     * @see HttpHeaders
     */
    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    /**
     * Deletes the "accessToken" cookie by {@link HttpHeaders#SET_COOKIE SET_COOKIE} a new cookie to the {@link HttpHeaders}.
     * <p> This cookie is returned by the {@link CookieUtil#deleteAccessTokenCookie() deleteAccessTokenCookie()} method in {@link CookieUtil}.
     * 
     * @see CookieUtil
     * @see CookieUtil#deleteAccessTokenCookie()
     * @see HttpHeaders
     */
    private void deleteAccessTokenCookie(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie().toString());
    }

}