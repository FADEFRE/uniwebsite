package swtp12.modulecrediting.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import swtp12.modulecrediting.dto.CredentialsDTO;
import swtp12.modulecrediting.dto.UserDTO;


@Component
public class UserAuthProvider implements AuthenticationProvider{

    private final AuthenticationService authenticationService;

    public UserAuthProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDTO userDto = null;

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            //auth by username and password
            userDto = authenticationService.authenticate(new CredentialsDTO(
                    (String) authentication.getPrincipal(),
                    (char[]) authentication.getCredentials()));
        } 
        else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            //auth by cookie
            userDto = authenticationService.findByToken((String) authentication.getPrincipal());
        }

        if (userDto == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
    
}
