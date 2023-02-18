package moe.pgnhd.theshop;

public class EmptyBasketException extends RuntimeException {
    public EmptyBasketException(String message) {
        super(message);
    }
}
