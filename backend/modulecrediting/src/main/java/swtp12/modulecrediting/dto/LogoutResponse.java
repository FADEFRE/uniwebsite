package swtp12.modulecrediting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class LogoutResponse {
    private SuccessFailure status;
    private String message;

    public enum SuccessFailure {
        SUCCESS, FAILURE , ERROR
    }
}
