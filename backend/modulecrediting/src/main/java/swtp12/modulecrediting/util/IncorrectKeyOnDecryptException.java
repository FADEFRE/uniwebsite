package swtp12.modulecrediting.util;

public class IncorrectKeyOnDecryptException extends Exception{
    public IncorrectKeyOnDecryptException(String errorMessage) {
        super(errorMessage);
    }
}
