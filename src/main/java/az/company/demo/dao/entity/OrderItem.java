package az.company.demo.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(nullable = false)
    Integer quantity;

    @Column(nullable = false, precision = 19, scale = 2, name = "unit_price")
    BigDecimal unitPrice;
}