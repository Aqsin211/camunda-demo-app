package az.company.demo.worker;

import az.company.demo.client.PaymentGatewayClient;
import az.company.demo.dao.entity.Order;
import az.company.demo.process.ProcessVariables;
import az.company.demo.service.OrderService;
import az.company.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ExternalTaskSubscription(topicName = "payment")
public class PaymentExternalTaskWorker implements ExternalTaskHandler {

    private static final int MAX_RETRIES = 2;
    private static final long RETRY_TIMEOUT_MS = 5_000L;

    private final PaymentGatewayClient paymentGatewayClient;
    private final PaymentService paymentService;
    private final OrderService orderService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        Long orderId = externalTask.getVariable(ProcessVariables.ORDER_ID);

        try {
            Order order = orderService.getEntityById(orderId);

            boolean paymentSuccessful = paymentGatewayClient.charge(orderId, order.getTotalAmount());

            if (paymentSuccessful) {
                paymentService.markSuccess(orderId);
            } else {
                paymentService.markFailed(orderId);
            }

            externalTaskService.complete(
                    externalTask,
                    Variables.putValue(ProcessVariables.PAYMENT_SUCCESSFUL, paymentSuccessful)
            );

        } catch (Exception e) {
            log.error("Unexpected error processing payment for order {}", orderId, e);

            // getRetries() is null on first failure - Camunda defaults it from
            // the BPMN retryTimeCycle if configured, otherwise treat as MAX_RETRIES.
            int remainingRetries = externalTask.getRetries() == null
                    ? MAX_RETRIES
                    : externalTask.getRetries() - 1;

            externalTaskService.handleFailure(
                    externalTask,
                    "Payment gateway call failed",
                    e.getMessage(),
                    remainingRetries,
                    RETRY_TIMEOUT_MS
            );
        }
    }
}