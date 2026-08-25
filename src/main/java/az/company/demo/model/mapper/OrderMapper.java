package az.company.demo.model.mapper;

import az.company.demo.dao.entity.Order;
import az.company.demo.model.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
}