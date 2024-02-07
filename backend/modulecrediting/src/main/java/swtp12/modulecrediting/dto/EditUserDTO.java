package swtp12.modulecrediting.dto;

import lombok.Data;
import swtp12.modulecrediting.model.Role;

@Data
public class EditUserDTO {
    private String id;
    private String username;
    private String password;
    private String passwordConfirm;
    private Role role;
}
