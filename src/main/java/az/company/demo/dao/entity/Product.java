package az.company.demo.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal price;

    @Column(nullable = false, name = "stock_quantity")
    Integer stockQuantity;
}