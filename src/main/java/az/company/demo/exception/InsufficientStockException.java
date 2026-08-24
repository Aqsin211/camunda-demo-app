package az.company.demo.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long productId) {
        super("Insufficient stock for product: " + productId);
    }
}