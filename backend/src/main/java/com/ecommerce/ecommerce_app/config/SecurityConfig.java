package com.ecommerce.ecommerce_app.config;

import com.ecommerce.ecommerce_app.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Poprawna wersja w Spring Security 6.1+
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Publiczne endpointy
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll() // User i Admin mogą czytać
                        .requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN") // Tylko Admin może dodawać
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN") // Tylko Admin może edytować
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN") // Tylko Admin może usuwać
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Brak sesji - używamy JWT
                .authenticationProvider(authenticationProvider()) // Ustawienie dostawcy uwierzytelniania
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Dodanie filtra JWT
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
