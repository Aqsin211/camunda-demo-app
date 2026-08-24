package az.company.demo.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(

        @NotNull
        Long productId,

        @NotNull
        @Positive
        Integer quantity
) {
}