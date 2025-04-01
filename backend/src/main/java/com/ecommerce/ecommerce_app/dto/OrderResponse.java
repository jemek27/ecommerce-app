package com.ecommerce.ecommerce_app.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long orderID;
    private String username;
    private LocalDateTime orderDate;
    private List<OrderedProductResponse> orderProducts;
}
