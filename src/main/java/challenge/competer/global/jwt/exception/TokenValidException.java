package challenge.competer.global.jwt.exception;

public class TokenValidException extends RuntimeException{

    public TokenValidException(String message) {
        super(message);
    }

    public TokenValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
