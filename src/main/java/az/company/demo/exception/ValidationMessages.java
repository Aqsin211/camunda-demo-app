package az.company.demo.exception;

public class ValidationMessages {

    // CreateOrderRequest
    public static final String CUSTOMER_ID_MUST_NOT_BE_NULL = "Customer id must not be null";
    public static final String ITEMS_MUST_NOT_BE_EMPTY = "Order must contain at least one item";

    // OrderItemRequest
    public static final String PRODUCT_ID_MUST_NOT_BE_NULL = "Product id must not be null";
    public static final String PRODUCT_ID_MUST_BE_POSITIVE = "Product id must be positive";
    public static final String QUANTITY_MUST_NOT_BE_NULL = "Quantity must not be null";
    public static final String QUANTITY_MUST_BE_POSITIVE = "Quantity must be positive";

    // ProductRequest
    public static final String NAME_MUST_NOT_BE_BLANK = "Product name must not be blank";
    public static final String PRICE_MUST_NOT_BE_NULL = "Price must not be null";
    public static final String PRICE_MUST_BE_POSITIVE_OR_ZERO = "Price must be positive or zero";
    public static final String STOCK_QUANTITY_MUST_NOT_BE_NULL = "Stock quantity must not be null";
    public static final String STOCK_QUANTITY_MUST_BE_POSITIVE_OR_ZERO = "Stock quantity must be positive or zero";

    private ValidationMessages() {
    }
}