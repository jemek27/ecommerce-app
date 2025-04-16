package com.ecommerce.ecommerce_app.dto;

import com.ecommerce.ecommerce_app.model.Role;
import com.ecommerce.ecommerce_app.dto.OrderResponse;
import com.ecommerce.ecommerce_app.dto.ReviewDTO;

import com.ecommerce.ecommerce_app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String username;
    private Role role;
    private List<ReviewDTO> reviews;
    private List<OrderResponse> orders;

    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.reviews = user.getReviews().stream().map(ReviewDTO::new).collect(Collectors.toList());
        this.orders = user.getOrder().stream().map(OrderResponse::new).collect(Collectors.toList());
    }
}
