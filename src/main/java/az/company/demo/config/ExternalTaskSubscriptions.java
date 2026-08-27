package az.company.demo.config;

import az.company.demo.worker.PaymentExternalTaskWorker;
import az.company.demo.worker.ShippingExternalTaskWorker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalTaskSubscriptions {

    private final ExternalTaskClient externalTaskClient;
    private final PaymentExternalTaskWorker paymentExternalTaskWorker;
    private final ShippingExternalTaskWorker shippingExternalTaskWorker;

    @PostConstruct
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