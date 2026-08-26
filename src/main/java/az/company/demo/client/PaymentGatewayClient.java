package az.company.demo.client;

import java.math.BigDecimal;

/**
 * Abstraction over the external payment provider.
 * Keeps the actual HTTP/SDK call out of the external task worker,
 * so the worker only deals with Camunda concerns (retries, variables)
 * and this client only deals with talking to the gateway.
 */
public interface PaymentGatewayClient {

    boolean charge(Long orderId, BigDecimal amount);
}
