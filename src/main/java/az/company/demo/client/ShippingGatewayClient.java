package az.company.demo.client;

/**
 * Abstraction over the external shipping provider.
 */
public interface ShippingGatewayClient {

    boolean dispatch(Long orderId);
}