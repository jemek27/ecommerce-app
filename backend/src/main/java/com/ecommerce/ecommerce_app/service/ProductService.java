package com.ecommerce.ecommerce_app.service;

import com.ecommerce.ecommerce_app.model.Product;
import com.ecommerce.ecommerce_app.model.Review;
import com.ecommerce.ecommerce_app.dto.ProductRequest;
import com.ecommerce.ecommerce_app.dto.ProductResponse;
import com.ecommerce.ecommerce_app.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id).map(this::mapToProductResponse);
    }

    public ProductResponse saveProduct(ProductRequest productInfo) {
        Product product = new Product();
        product.setName(productInfo.getName());
        product.setPrice(productInfo.getPrice());
        product.setDescription(productInfo.getDescription());
        return mapToProductResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponse mapToProductResponse(Product product) {
        List<Review> reviews = product.getReviews();

        double avgRating = reviews.isEmpty() ? 0.0 :
                reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                avgRating,
                reviews.size()
        );
    }
}
