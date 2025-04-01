package com.ecommerce.ecommerce_app.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class OrderedProductResponse {
    private Long id;
    private String name;
    private double totalPrice;
    private int quantity;
}
