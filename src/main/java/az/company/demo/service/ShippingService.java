package az.company.demo.service;

import az.company.demo.dao.entity.Shipment;
import az.company.demo.dao.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShipmentRepository shipmentRepository;

    public Shipment getByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId)
                .orElse(null);
    }
}