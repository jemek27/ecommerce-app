package com.ecommerce.ecommerce_app.service;

import com.ecommerce.ecommerce_app.dto.AuthenticationRequest;
import com.ecommerce.ecommerce_app.dto.AuthenticationResponse;
import com.ecommerce.ecommerce_app.dto.RefreshTokenRequest;
import com.ecommerce.ecommerce_app.dto.RegisterRequest;
import com.ecommerce.ecommerce_app.model.RefreshToken;
import com.ecommerce.ecommerce_app.model.Role;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.repository.RefreshTokenRepository;
import com.ecommerce.ecommerce_app.repository.UserRepository;
import com.ecommerce.ecommerce_app.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId());
        String refreshToken = generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return "The user already exists";
        }

        // Check if the role is empty, if so, set the default role USER
        Role role = request.getRole() != null ? request.getRole() : Role.USER;

        User newUser = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), role);
        userRepository.save(newUser);
        return "Registration successfully completed";
    }

    public ResponseEntity<?> refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token expired");
        }

        User user = storedToken.getUser();

        refreshTokenRepository.delete(storedToken);
        String newRefreshToken = generateRefreshToken(user);

        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId());

        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, newRefreshToken));
    }

    private String generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtUtil.generateSecureToken());
        refreshToken.setExpiryDate(Instant.now().plus(3, ChronoUnit.DAYS));

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
