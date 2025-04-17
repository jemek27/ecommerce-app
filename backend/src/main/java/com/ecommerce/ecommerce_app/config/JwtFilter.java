package com.ecommerce.ecommerce_app.config;

import com.ecommerce.ecommerce_app.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        // Lista publicznych endpointów
        if (path.startsWith("/auth/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs/") ||
                (path.startsWith("/reviews/") && method.equals("GET")) ||
                (path.startsWith("/products/") && method.equals("GET"))) {
            chain.doFilter(request, response); // Przepuść żądanie bez walidacji tokenu
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token has expired!");
                sendCorsErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired!");
                return;
            } catch (Exception e) {
                logger.warn("Invalid JWT token!");
                sendCorsErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token!");
                return;
            }
        } else {
            logger.warn("Missing Authorization header!");
            sendCorsErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header!");
            return;
        }

        // If the token is valid and the user is not yet authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // We are creating an Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Set authorization details in the context of security
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // We register the user as authenticated
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            logger.warn("JWT token is invalid!");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is invalid!");
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendCorsErrorResponse(HttpServletRequest request, HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setStatus(statusCode);
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
