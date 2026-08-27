package az.company.demo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mock implementation for demo/portfolio purposes.
 */
@Slf4j
@Component
public class MockShippingGatewayClient implements ShippingGatewayClient {

    @Override
    public boolean dispatch(Long orderId) {

        boolean success = true;

        log.info("Mock dispatching shipment for order {} -> {}", orderId, "SUCCESS");

        return success;
    }
}