package swtp12.modulecrediting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import swtp12.modulecrediting.dto.CustomUserDetails;
import swtp12.modulecrediting.dto.LoginRequest;
import swtp12.modulecrediting.dto.UserSummary;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.UserRepository;

/**
 * {@link UserService} is a {@link Service} and provides the {@link #login(LoginRequest, String, String) login},
 * {@link #refresh(String, String) refresh}, {@link #logout() logout} 
 * and {@link #getUserProfile() getUserProfile} methods.
 * 
 * @see #login(LoginRequest, String, String)
 * @see #refresh(String, String)
 * @see #logout()
 * @see #getUserProfile()
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html"> Springboot @Service </a>
 * 
 * @author Frederik Kluge 
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * This method gets the {@link UserSummary} of a current authenticated {@link User}. 
     * 
     * @return {@code UserSummary} of the authenticated {@code User}.
     * 
     * @throws IllegalArgumentException if {@link User} could not be found in the database with the given {@code username}.
     * 
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
        return user.toUserSummaryId();
    }

}