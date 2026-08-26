package az.company.demo.controller;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentWebhookController {

    private final RuntimeService runtimeService;

    @PostMapping("/{orderId}/delivered")
    public ResponseEntity<Void> markDelivered(@PathVariable Long orderId) {
        runtimeService.createMessageCorrelation("shipmentDelivered")
                .processInstanceBusinessKey(orderId.toString())
                .correlate();
        return ResponseEntity.ok().build();
    }
}