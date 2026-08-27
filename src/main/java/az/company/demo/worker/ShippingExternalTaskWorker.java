package az.company.demo.worker;

import az.company.demo.client.ShippingGatewayClient;
import az.company.demo.process.ProcessVariables;
import az.company.demo.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShippingExternalTaskWorker implements ExternalTaskHandler {

    private static final int MAX_RETRIES = 2;
    private static final long RETRY_TIMEOUT_MS = 5_000L;

    private final ShippingGatewayClient shippingGatewayClient;
    private final ShippingService shippingService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        Long orderId = externalTask.getVariable(ProcessVariables.ORDER_ID);

        try {
            shippingGatewayClient.dispatch(orderId);
            shippingService.markShipped(orderId);

            externalTaskService.complete(externalTask);

        } catch (Exception e) {
            log.error("Unexpected error dispatching shipment for order {}", orderId, e);

            int remainingRetries = externalTask.getRetries() == null
                    ? MAX_RETRIES
                    : externalTask.getRetries() - 1;

            externalTaskService.handleFailure(
                    externalTask,
                    "Shipping gateway call failed",
                    e.getMessage(),
                    remainingRetries,
                    RETRY_TIMEOUT_MS
            );
        }
    }
}