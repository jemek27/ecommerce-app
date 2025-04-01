package com.ecommerce.ecommerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest{
    private String name;
    private double price;
    private String description;
}
