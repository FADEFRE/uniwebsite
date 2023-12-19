package swtp12.modulecrediting.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {
    
    @NotEmpty
    private String username;

    @NotEmpty
    private char[] password;

    public SignUpDTO() {
        super();
    }

    public SignUpDTO(@NotEmpty String username, @NotEmpty char[] password) {
        this.username = username;
        this.password = password;
    }

}
