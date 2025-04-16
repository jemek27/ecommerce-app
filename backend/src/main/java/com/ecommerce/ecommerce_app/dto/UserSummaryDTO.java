package com.ecommerce.ecommerce_app.dto;

import com.ecommerce.ecommerce_app.model.Role;
import com.ecommerce.ecommerce_app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSummaryDTO {
    private Long id;
    private String username;
    private Role role;

    public UserSummaryDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}

