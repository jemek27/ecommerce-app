package com.ecommerce.ecommerce_app.repository;

import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user); //mabye user id?
}