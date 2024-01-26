package swtp12.modulecrediting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import swtp12.modulecrediting.model.Role;

@Data
public class RegisterRequest {
    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "PasswordConfirm cannot be empty")
    private String passwordConfirm;

    @NotBlank(message = "Role cannot be empty")
    private Role role;
}
