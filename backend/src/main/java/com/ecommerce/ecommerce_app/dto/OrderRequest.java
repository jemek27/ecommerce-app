package com.ecommerce.ecommerce_app.dto;

import com.ecommerce.ecommerce_app.model.User;
import lombok.Data;

import java.util.Map;

@Data
public class OrderRequest {
    private User user;
    private Map<Long, Integer> productQuantities; // ID produktu + ilość{
}
