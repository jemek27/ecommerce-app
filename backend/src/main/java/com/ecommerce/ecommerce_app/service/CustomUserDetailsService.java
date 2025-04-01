package com.ecommerce.ecommerce_app.service;

import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.model.Role;
import com.ecommerce.ecommerce_app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void createUser(String username, String encodedPassword, Role role) {
        User user = new User(username, encodedPassword, role);
        userRepository.save(user);
    }

    public User getUserById(Long userId) { return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")); }
}

