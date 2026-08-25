package az.company.demo.dao.entity;

import az.company.demo.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, name = "order_id")
    Long orderId;

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PaymentStatus status;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "paid_at")
    LocalDateTime paidAt;
}