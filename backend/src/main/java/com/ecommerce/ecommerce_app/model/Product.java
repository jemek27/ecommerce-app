package com.ecommerce.ecommerce_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String description;
}
