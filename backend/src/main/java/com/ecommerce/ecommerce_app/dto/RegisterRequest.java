package com.ecommerce.ecommerce_app.dto;

import com.ecommerce.ecommerce_app.model.Role;
import lombok.*;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
