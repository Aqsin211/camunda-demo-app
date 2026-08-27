package az.company.demo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class MockShippingGatewayClient implements ShippingGatewayClient {

    private static final int SUCCESS_RATE_PERCENT = 100;

    @Override
    public boolean dispatch(Long orderId) {

        boolean success = ThreadLocalRandom.current().nextInt(100) < SUCCESS_RATE_PERCENT;

        log.info("Mock dispatching shipment for order {} -> {}", orderId, success ? "SUCCESS" : "FAILED");

        return success;
    }
}