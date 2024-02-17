package swtp12.modulecrediting.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import swtp12.modulecrediting.service.CustomUserDetailsServiceImpl;
import swtp12.modulecrediting.service.TokenProvider;
import swtp12.modulecrediting.util.IncorrectKeyOnDecryptException;
import swtp12.modulecrediting.util.SecurityCipher;

public class TokenAuthenticationFilter extends OncePerRequestFilter{
    @Value("${app.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${app.auth.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtToken(httpServletRequest, true);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } 
        catch (UsernameNotFoundException uEx) {
            System.out.println("doFilterInternal UsernameNotFoundException: This might be thrown due to a change of username by the user and SpringSecurities automatic doFilterInternal on the logout request");
        }
        catch (Exception ex) {
            System.out.println("----------doFilterInternal error stacktrace start:-------------");
            ex.printStackTrace();
            System.out.println("----------end of doFilterInternal error stacktrace-------------");
        } 
        finally {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) throws IncorrectKeyOnDecryptException {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);
            if (accessToken == null) return null;

            return SecurityCipher.decrypt(accessToken);
        }
        return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) throws IncorrectKeyOnDecryptException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (accessTokenCookieName.equals(cookie.getName())) {
                String accessToken = cookie.getValue();
                if (accessToken == null) return null;

                return SecurityCipher.decrypt(accessToken);
            }
        }
        return null;
    }

    private String getJwtToken(HttpServletRequest request, boolean fromCookie) throws IncorrectKeyOnDecryptException {
        if (fromCookie) return getJwtFromCookie(request);

        return getJwtFromRequest(request);
    }
}
