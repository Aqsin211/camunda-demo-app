package az.company.demo.delegate;

import az.company.demo.dao.entity.Order;
import az.company.demo.service.InventoryService;
import az.company.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("checkStockDelegate")
@RequiredArgsConstructor
public class CheckStockDelegate implements JavaDelegate {

    private final OrderService orderService;
    private final InventoryService inventoryService;

    @Override
    public void execute(DelegateExecution execution) {

        Long orderId = (Long) execution.getVariable("orderId");

        Order order = orderService.getById(orderId);

        boolean stockAvailable =
                inventoryService.isStockAvailable(order);

        execution.setVariable(
                "stockAvailable",
                stockAvailable
        );
    }
}