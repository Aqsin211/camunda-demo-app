package az.company.demo.model.dto.request;

import az.company.demo.exception.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(

        @NotNull(message = ValidationMessages.CUSTOMER_ID_MUST_NOT_BE_NULL)
        Long customerId,

        @NotEmpty(message = ValidationMessages.ITEMS_MUST_NOT_BE_EMPTY)
        List<@Valid OrderItemRequest> items
) {
}