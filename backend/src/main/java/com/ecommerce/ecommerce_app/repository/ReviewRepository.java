package com.ecommerce.ecommerce_app.repository;

import com.ecommerce.ecommerce_app.model.Review;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser(User user);
    List<Review> findByProduct(Product product);
}

