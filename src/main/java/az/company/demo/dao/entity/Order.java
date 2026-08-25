package az.company.demo.dao.entity;

import az.company.demo.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false, name = "customer_id")
    Long customerId;

    @Column(nullable = false, precision = 19, scale = 2, name = "total_amount")
    BigDecimal totalAmount;

    @Enumerated(STRING)
    @Column(nullable = false)
    OrderStatus status;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<OrderItem> items = new ArrayList<>();
}