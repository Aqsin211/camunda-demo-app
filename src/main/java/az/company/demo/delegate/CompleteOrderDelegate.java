package az.company.demo.delegate;

import az.company.demo.model.enums.OrderStatus;
import az.company.demo.process.ProcessVariables;
import az.company.demo.service.OrderService;
import az.company.demo.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("completeOrderDelegate")
@RequiredArgsConstructor
public class CompleteOrderDelegate implements JavaDelegate {

    private final OrderService orderService;
    private final ShippingService shippingService;

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable(ProcessVariables.ORDER_ID);

        shippingService.markDelivered(orderId);
        orderService.updateStatus(orderId, OrderStatus.COMPLETED);
    }
}