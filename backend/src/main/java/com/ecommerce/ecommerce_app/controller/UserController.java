package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.dto.UserProfileDTO;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final CustomUserDetailsService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile(Authentication authentication) {
        if (authentication == null) { return ResponseEntity.status(401).build(); }

        String username = authentication.getName();

        if (authentication.getName() == null) { return ResponseEntity.status(401).build(); }

        User user = userService.getUserByUsername(username);

        if (user == null) { return ResponseEntity.status(404).body(null); }

        return ResponseEntity.ok(new UserProfileDTO(user));
    }
}
