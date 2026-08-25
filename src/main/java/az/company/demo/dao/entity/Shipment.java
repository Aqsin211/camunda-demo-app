package az.company.demo.dao.entity;

import az.company.demo.model.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Shipment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, name = "order_id")
    Long orderId;

    @Column(unique = true, name = "tracking_number")
    String trackingNumber;

    @Enumerated(STRING)
    @Column(nullable = false)
    ShipmentStatus status;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "shipped_at")
    LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    LocalDateTime deliveredAt;
}