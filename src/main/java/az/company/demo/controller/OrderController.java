package az.company.demo.controller;

import az.company.demo.dao.entity.Order;
import az.company.demo.model.dto.request.CreateOrderRequest;
import az.company.demo.model.dto.response.OrderResponse;
import az.company.demo.model.mapper.OrderMapper;
import az.company.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {

        Order order = orderService.createOrder(request);

        return orderMapper.toResponse(order);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @PathVariable Long orderId
    ) {

        Order order = orderService.getById(orderId);

        return orderMapper.toResponse(order);
    }
}