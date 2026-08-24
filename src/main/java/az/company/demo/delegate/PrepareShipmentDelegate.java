package az.company.demo.delegate;

import az.company.demo.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("prepareShipmentDelegate")
@RequiredArgsConstructor
public class PrepareShipmentDelegate implements JavaDelegate {

    private final ShippingService shippingService;

    @Override
    public void execute(DelegateExecution execution) {

        Long orderId = (Long) execution.getVariable("orderId");

        // Shipment creation will be implemented later.
        // This delegate currently represents the BPMN step.
    }
}