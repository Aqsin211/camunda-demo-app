package az.company.demo.model.dto.response;

import az.company.demo.model.enums.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        Long customerId,
        BigDecimal totalAmount,
        OrderStatus status
) {
}