package swtp12.modulecrediting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import swtp12.modulecrediting.dto.EditUserDTO;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.repository.RoleRepository;
import swtp12.modulecrediting.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository; 

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testDeleteUser() {
        EditUserDTO deleteRequest = new EditUserDTO();
        deleteRequest.setId(1L);

        when(userRepository.findById(deleteRequest.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(deleteRequest));
    }

    @Test
    void testChangeUsername() {
        EditUserDTO changeRequest = new EditUserDTO();
        changeRequest.setId(1L);
        changeRequest.setUsername("newUsername");

        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(changeRequest.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(ResponseStatusException.class, () -> userService.changeUsername(changeRequest));
    }

    @Test
    void testChangeRole() {
        EditUserDTO changeRequest = new EditUserDTO();
        changeRequest.setId(1L);
        changeRequest.setRole("ROLE_ADMIN");

        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.changeRole(changeRequest));
    }

    @Test
    void testChangePassword() {
        EditUserDTO changeRequest = new EditUserDTO();
        changeRequest.setId(1L);
        changeRequest.setPassword("newPassword");
        changeRequest.setPasswordConfirm("newPassword");

        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.changePassword(changeRequest));
    }

    @Test
    void testRegister() {
        EditUserDTO registerRequest = new EditUserDTO();
        registerRequest.setUsername("newUser");
        registerRequest.setPassword("password");
        registerRequest.setPasswordConfirm("password");
        registerRequest.setRole("ROLE_USER");
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(registerRequest.getRole())).thenReturn(Optional.of(new Role())); // Mock RoleRepository
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> userService.register(registerRequest));
    }   
}
