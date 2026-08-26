package az.company.demo.delegate;

import az.company.demo.dao.entity.Order;
import az.company.demo.model.enums.OrderStatus;
import az.company.demo.process.ProcessVariables;
import az.company.demo.service.OrderService;
import az.company.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("preparePaymentDelegate")
@RequiredArgsConstructor
public class PreparePaymentDelegate implements JavaDelegate {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) {

        Long orderId = (Long) execution.getVariable(ProcessVariables.ORDER_ID);
        Order order = orderService.getEntityById(orderId);

        paymentService.createPending(orderId, order.getTotalAmount());
        orderService.updateStatus(orderId, OrderStatus.PAYMENT_PENDING);
    }
}