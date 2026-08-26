package az.company.demo.service;

import az.company.demo.dao.entity.Shipment;
import az.company.demo.dao.repository.ShipmentRepository;
import az.company.demo.model.enums.ShipmentStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Transactional
    public Shipment createPreparing(Long orderId) {
        Shipment shipment = Shipment.builder()
                .orderId(orderId)
                .status(ShipmentStatus.PREPARING)
                .createdAt(LocalDateTime.now())
                .build();
        return shipmentRepository.save(shipment);
    }
}