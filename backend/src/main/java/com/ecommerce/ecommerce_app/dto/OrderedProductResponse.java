package com.ecommerce.ecommerce_app.dto;

import com.ecommerce.ecommerce_app.model.Product;
import com.ecommerce.ecommerce_app.model.OrderProduct;
import lombok.*;

@Data
@AllArgsConstructor
public class OrderedProductResponse {
    private Long id;
    private String name;
    private double totalPrice;
    private int quantity;

    public OrderedProductResponse(OrderProduct orderProduct) {
        this.id = orderProduct.getProduct().getId();
        this.name = orderProduct.getProduct().getName();
        this.totalPrice = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        this.quantity = orderProduct.getQuantity();
    }
}
