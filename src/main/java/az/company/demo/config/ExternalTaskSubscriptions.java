package az.company.demo.config;

import az.company.demo.worker.PaymentExternalTaskWorker;
import az.company.demo.worker.ShippingExternalTaskWorker;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalTaskSubscriptions {

    private final ExternalTaskClient externalTaskClient;
    private final PaymentExternalTaskWorker paymentExternalTaskWorker;
    private final ShippingExternalTaskWorker shippingExternalTaskWorker;

    @EventListener(ApplicationReadyEvent.class)
    public void subscribe() {
        externalTaskClient
                .subscribe("payment")
                .handler(paymentExternalTaskWorker)
                .open();

        externalTaskClient
                .subscribe("shipping")
                .handler(shippingExternalTaskWorker)
                .open();
    }
}