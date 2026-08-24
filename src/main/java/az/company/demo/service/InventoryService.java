package az.company.demo.service;

import az.company.demo.dao.entity.Order;
import az.company.demo.dao.entity.OrderItem;
import az.company.demo.dao.entity.Product;
import az.company.demo.dao.repository.ProductRepository;
import az.company.demo.exception.InsufficientStockException;
import az.company.demo.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public boolean isStockAvailable(Order order) {

        for (OrderItem item : order.getItems()) {

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() ->
                            new ProductNotFoundException(
                                    item.getProduct().getId()
                            )
                    );

            if (product.getStockQuantity() < item.getQuantity()) {
                return false;
            }
        }

        return true;
    }

    @Transactional
    public void reserveStock(Order order) {

        for (OrderItem item : order.getItems()) {

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() ->
                            new ProductNotFoundException(
                                    item.getProduct().getId()
                            )
                    );

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(product.getId());
            }

            product.setStockQuantity(
                    product.getStockQuantity() - item.getQuantity()
            );

            productRepository.save(product);
        }
    }
}