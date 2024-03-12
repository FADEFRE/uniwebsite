package swtp12.modulecrediting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for the Login request
 */
@Data
public class LoginRequest {
    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
