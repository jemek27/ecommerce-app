package com.ecommerce.ecommerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private double price;
    private String description;
    private double avgRating;
    private int numberOfRatings;
}
