package swtp12.modulecrediting.dto;

import lombok.Data;

@Data
public class EditUserDTO {
    private Long id;
    private String username;
    private String password;
    private String passwordConfirm;
    private String role;
}
