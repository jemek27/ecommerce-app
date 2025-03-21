package com.ecommerce.ecommerce_app.dto;

import com.ecommerce.ecommerce_app.model.Role;
import lombok.*;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
    private Role role;
}