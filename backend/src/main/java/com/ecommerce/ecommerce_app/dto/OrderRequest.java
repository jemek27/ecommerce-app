package com.ecommerce.ecommerce_app.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrderRequest {
    private Long userID;
    private Map<Long, Integer> productQuantities; // ID produktu + ilość
}
