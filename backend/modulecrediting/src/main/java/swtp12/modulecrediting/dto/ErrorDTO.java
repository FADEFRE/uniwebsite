package swtp12.modulecrediting.dto;

public class ErrorDTO {

    private String message;

    public ErrorDTO() {
        super();
    }

    public ErrorDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
