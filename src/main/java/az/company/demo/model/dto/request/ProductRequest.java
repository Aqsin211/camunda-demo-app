package az.company.demo.model.dto.request;

import az.company.demo.exception.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = ValidationMessages.NAME_MUST_NOT_BE_BLANK)
        String name,

        @NotNull(message = ValidationMessages.PRICE_MUST_NOT_BE_NULL)
        @PositiveOrZero(message = ValidationMessages.PRICE_MUST_BE_POSITIVE_OR_ZERO)
        BigDecimal price,

        @NotNull(message = ValidationMessages.STOCK_QUANTITY_MUST_NOT_BE_NULL)
        @PositiveOrZero(message = ValidationMessages.STOCK_QUANTITY_MUST_BE_POSITIVE_OR_ZERO)
        Integer stockQuantity
) {
}