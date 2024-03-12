package swtp12.modulecrediting.dto;

import lombok.Data;
import swtp12.modulecrediting.model.User;

/**
 * DTO for creating and updating a {@link User}
 */
@Data
public class EditUserDTO {
    private Long id;
    private String username;
    private String password;
    private String passwordConfirm;
    private String role;
}
