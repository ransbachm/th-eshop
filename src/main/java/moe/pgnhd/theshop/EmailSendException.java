package moe.pgnhd.theshop;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message, Exception e) {
        super(message, e);
    }
}
