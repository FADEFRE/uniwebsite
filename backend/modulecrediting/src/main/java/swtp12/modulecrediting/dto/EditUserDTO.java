package swtp12.modulecrediting.dto;

import lombok.Data;

@Data
public class EditUserDTO {
    private String id;
    private String username;
    private String password;
    private String passwordConfirm;
    private String role;
}
