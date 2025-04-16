package com.ecommerce.ecommerce_app.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.model.User;
import lombok.*;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long orderID;
    private LocalDateTime orderDate;
    private List<OrderedProductResponse> orderProducts;

    public OrderResponse(Order order) {
        this.orderID = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderProducts = order.getOrderProducts().stream()
                            .map(OrderedProductResponse::new)
                            .collect(Collectors.toList());
    }
}
