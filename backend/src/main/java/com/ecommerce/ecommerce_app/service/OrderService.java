package com.ecommerce.ecommerce_app.service;

import com.ecommerce.ecommerce_app.dto.OrderResponse;
import com.ecommerce.ecommerce_app.dto.OrderedProductResponse;
import com.ecommerce.ecommerce_app.model.Product;
import com.ecommerce.ecommerce_app.model.OrderProduct;
import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.repository.ProductRepository;
import com.ecommerce.ecommerce_app.repository.OrderRepository;
import com.ecommerce.ecommerce_app.repository.OrderProductRepository;
import com.ecommerce.ecommerce_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderResponse createOrder(Long userId, Map<Long, Integer> productQuantities) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order = orderRepository.save(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productRepository.getReferenceById(entry.getKey()); // Getting a reference instead of an object

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(entry.getValue());
            orderProducts.add(orderProduct);
        }

        orderProductRepository.saveAll(orderProducts);
        order.setOrderProducts(orderProducts);
        orderRepository.save(order);

        return mapToOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::mapToOrderResponse);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getUsername(),
                order.getOrderDate(),
                order.getOrderProducts().stream()
                        .map(this::mapToOrderedProductResponse)
                        .collect(Collectors.toList())
        );
    }

    private OrderedProductResponse mapToOrderedProductResponse(OrderProduct orderProduct) {
        return new OrderedProductResponse(
                orderProduct.getProduct().getId(),
                orderProduct.getProduct().getName(),
                orderProduct.getProduct().getPrice() * orderProduct.getQuantity(),
                orderProduct.getQuantity()
        );
    }
}
