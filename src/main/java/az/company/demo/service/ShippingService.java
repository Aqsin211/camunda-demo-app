package az.company.demo.service;

import az.company.demo.dao.entity.Shipment;
import az.company.demo.dao.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {

    private final ShipmentRepository shipmentRepository;

    public ShippingService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Shipment getByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId)
                .orElse(null);
    }
}