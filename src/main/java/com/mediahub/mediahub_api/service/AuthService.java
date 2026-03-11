package com.mediahub.mediahub_api.service;

import com.mediahub.mediahub_api.dto.AuthResponse;
import com.mediahub.mediahub_api.dto.LoginRequest;
import com.mediahub.mediahub_api.dto.UserResponse;
import com.mediahub.mediahub_api.model.User;
import com.mediahub.mediahub_api.repository.UserRepository;
import com.mediahub.mediahub_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest  loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return new AuthResponse(
                token,
                jwtService.expiration(),
                userResponse
        );
    }
}
