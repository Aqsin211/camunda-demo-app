package az.company.demo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Mock implementation for demo/portfolio purposes.
 */
@Slf4j
@Component
public class MockPaymentGatewayClient implements PaymentGatewayClient {

    private static final int SUCCESS_RATE_PERCENT = 85;

    @Override
    public boolean charge(Long orderId, BigDecimal amount) {

        boolean success = ThreadLocalRandom.current().nextInt(100) < SUCCESS_RATE_PERCENT;

        log.info("Mock charging order {} for amount {} -> {}", orderId, amount, success ? "SUCCESS" : "DECLINED");

        return success;
    }
}