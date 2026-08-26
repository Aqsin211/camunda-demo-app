package az.company.demo.delegate;

import az.company.demo.model.enums.OrderStatus;
import az.company.demo.service.OrderService;
import az.company.demo.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("prepareShipmentDelegate")
@RequiredArgsConstructor
public class PrepareShipmentDelegate implements JavaDelegate {

    private final ShippingService shippingService;
    private final OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");

        shippingService.createPreparing(orderId);
        orderService.updateStatus(orderId, OrderStatus.SHIPPING);
    }
}