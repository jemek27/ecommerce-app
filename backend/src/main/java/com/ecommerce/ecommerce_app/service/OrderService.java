package com.ecommerce.ecommerce_app.service;

import com.ecommerce.ecommerce_app.model.Product;
import com.ecommerce.ecommerce_app.model.OrderProduct;
import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.repository.ProductRepository;
import com.ecommerce.ecommerce_app.repository.OrderRepository;
import com.ecommerce.ecommerce_app.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public Order createOrder(User user, Map<Long, Integer> productQuantities) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order = orderRepository.save(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(entry.getValue());
            orderProducts.add(orderProduct);
        }

        orderProductRepository.saveAll(orderProducts);
        order.setOrderProducts(orderProducts);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
