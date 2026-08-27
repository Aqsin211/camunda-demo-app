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
import az.company.demo.model.dto.response.OrderResponse;
import az.company.demo.model.enums.OrderStatus;
import az.company.demo.model.mapper.OrderMapper;
import az.company.demo.process.ProcessVariables;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private static final String PROCESS_DEFINITION_KEY = "Process_0diemk0";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RuntimeService runtimeService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        RuntimeService runtimeService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.runtimeService = runtimeService;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

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

        Order saved = orderRepository.save(order);

        startProcessInstance(saved);

        return OrderMapper.toResponse(saved);
    }

    private void startProcessInstance(Order order) {

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY,
                order.getId().toString(),
                Variables.putValue(ProcessVariables.ORDER_ID, order.getId())
                        .putValue(ProcessVariables.TOTAL_AMOUNT, order.getTotalAmount().doubleValue())
        );

        order.setProcessInstanceId(instance.getId());
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long orderId) {

        return orderRepository.findById(orderId)
                .map(OrderMapper::toResponse)
                .orElseThrow(() ->
                        new OrderNotFoundException(orderId)
                );
    }

    @Transactional(readOnly = true)
    public Order getEntityById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(orderId)
                );
    }

    @Transactional
    public void validateOrder(Long orderId) {

        Order order = getEntityById(orderId);

        if (order.getItems().isEmpty()) {
            throw new InvalidOrderException(
                    "Order must contain at least one item"
            );
        }

        order.setStatus(OrderStatus.VALIDATED);
    }

    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = getEntityById(orderId);

        order.setStatus(OrderStatus.CANCELLED);
    }

    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {

        Order order = getEntityById(orderId);

        order.setStatus(status);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderMapper::toResponse).toList();
    }
}