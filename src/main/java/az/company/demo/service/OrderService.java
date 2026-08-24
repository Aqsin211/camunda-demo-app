package az.company.demo.service;

import az.company.demo.dao.entity.Order;
import az.company.demo.dao.entity.OrderItem;
import az.company.demo.dao.entity.Product;
import az.company.demo.dao.repository.OrderRepository;
import az.company.demo.dao.repository.ProductRepository;
import az.company.demo.exception.InvalidOrderException;
import az.company.demo.exception.OrderNotFoundException;
import az.company.demo.model.dto.request.CreateOrderRequest;
import az.company.demo.model.dto.request.OrderItemRequest;
import az.company.demo.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        if (request.items().isEmpty()) {
            throw new InvalidOrderException(
                    "Order must contain at least one item"
            );
        }

        Order order = Order.builder()
                .customerId(request.customerId())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {

            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() ->
                            new InvalidOrderException(
                                    "Product not found: " + itemRequest.productId()
                            )
                    );

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .unitPrice(product.getPrice())
                    .build();

            order.getItems().add(item);

            total = total.add(
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(itemRequest.quantity())
                            )
            );
        }

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(orderId)
                );
    }

    @Transactional
    public void validateOrder(Long orderId) {

        Order order = getById(orderId);

        if (order.getItems().isEmpty()) {
            throw new InvalidOrderException(
                    "Order must contain at least one item"
            );
        }

        order.setStatus(OrderStatus.VALIDATED);
    }

    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = getById(orderId);

        order.setStatus(OrderStatus.CANCELLED);
    }
}