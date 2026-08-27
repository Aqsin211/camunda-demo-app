package az.company.demo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mock implementation for demo/portfolio purposes.
 */
@Slf4j
@Component
public class MockPaymentGatewayClient implements PaymentGatewayClient {

    @Override
    public boolean charge(Long orderId, BigDecimal amount) {

        boolean success = true;

        log.info("Mock charging order {} for amount {} -> {}", orderId, amount, "SUCCESS");

        return success;
    }
}