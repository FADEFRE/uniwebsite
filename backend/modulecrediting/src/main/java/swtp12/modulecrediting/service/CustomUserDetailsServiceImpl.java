package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import swtp12.modulecrediting.dto.CustomUserDetails;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;

/**
 * This is an implementation of the {@code UserDetailsService}
 * @author Frederik Kluge
 * @see #loadUserByUsername
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Spring Service</a>
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetailsService.html">Spring UserDetailsService</a>
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    /**
     * This method overrides the {@code loadUserByUsername}
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("User with this username not found: " + s));
        return new CustomUserDetails(user);
    }
}
