package swtp12.modulecrediting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for the Login response
 */
@Data @AllArgsConstructor
public class LoginResponse {
    private SuccessFailure status;
    private String message;

    /**
     * Enum if Login was Success or Failure
     */
    public enum SuccessFailure {
        SUCCESS, FAILURE
    }
}
