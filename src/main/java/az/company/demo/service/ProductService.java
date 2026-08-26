package az.company.demo.service;

import az.company.demo.dao.repository.ProductRepository;
import az.company.demo.model.dto.request.ProductRequest;
import az.company.demo.model.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
    }

    public ProductResponse createProduct(@Valid ProductRequest productRequest) {
    }

    public ProductResponse getProductById(Long id) {
    }
}
