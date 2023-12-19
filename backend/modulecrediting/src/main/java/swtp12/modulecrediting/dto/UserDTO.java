package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    
    private Long id;
    private String username;
    private String token;

    public UserDTO() {
        super();
    }

    public UserDTO(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

}
