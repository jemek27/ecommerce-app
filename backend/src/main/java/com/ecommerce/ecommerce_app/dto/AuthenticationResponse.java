package com.ecommerce.ecommerce_app.dto;

import lombok.*;

@Data
public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
