package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.dto.RefreshTokenRequest;
import com.ecommerce.ecommerce_app.service.AuthenticationService;
import com.ecommerce.ecommerce_app.dto.AuthenticationRequest;
import com.ecommerce.ecommerce_app.dto.AuthenticationResponse;
import com.ecommerce.ecommerce_app.dto.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse = authenticationService.login(request);
        authenticationService.setRefreshTokenCookie(response, authResponse.getRefreshToken());
        return new AuthenticationResponse(authResponse.getAccessToken(), null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Pobieramy refresh token z cookie
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak refresh tokenu");
        }

        ResponseEntity<?> resp = authenticationService.refreshToken(refreshToken, response);
        return resp;
    }
}


