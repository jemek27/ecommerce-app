package com.ecommerce.ecommerce_app.dto;

import com.ecommerce.ecommerce_app.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String username;
    private String productName;
    private short rating;
    private String comment;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.username = review.getUser().getUsername();
        this.productName = review.getProduct().getName();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}

