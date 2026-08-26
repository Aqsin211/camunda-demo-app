package az.company.demo.delegate;

import az.company.demo.dao.entity.Order;
import az.company.demo.exception.InsufficientStockException;
import az.company.demo.model.enums.OrderStatus;
import az.company.demo.service.InventoryService;
import az.company.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("reserveStockDelegate")
@RequiredArgsConstructor
public class ReserveStockDelegate implements JavaDelegate {

    private final OrderService orderService;
    private final InventoryService inventoryService;

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        Order order = orderService.getEntityById(orderId);

        try {
            inventoryService.reserveStock(order);
        } catch (InsufficientStockException e) {
            execution.setVariable("stockAvailable", false);
            throw new BpmnError("INSUFFICIENT_STOCK", e.getMessage());
        }

        orderService.updateStatus(orderId, OrderStatus.STOCK_RESERVED);
        execution.setVariable("stockReserved", true);
    }
}