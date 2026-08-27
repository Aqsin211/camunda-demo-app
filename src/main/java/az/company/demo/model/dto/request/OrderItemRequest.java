package az.company.demo.model.dto.request;

import az.company.demo.exception.ValidationMessages;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(

        @NotNull(message = ValidationMessages.PRODUCT_ID_MUST_NOT_BE_NULL)
        @Positive(message = ValidationMessages.PRODUCT_ID_MUST_BE_POSITIVE)
        Long productId,

        @NotNull(message = ValidationMessages.QUANTITY_MUST_NOT_BE_NULL)
        @Positive(message = ValidationMessages.QUANTITY_MUST_BE_POSITIVE)
        Integer quantity
) {
}