package swtp12.modulecrediting.dto;

import lombok.Data;

/**
 * DTO for User data responses
 */
@Data
public class UserSummary {
    private String username;
    private Long userId;
    private String role;
}
