package com.mediahub.mediahub_api.service;

import com.mediahub.mediahub_api.dto.*;
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

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtService.getAccessTokenInSeconds(),
                userResponse
        );
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest  refreshTokenRequest) {

        String email = jwtService.extractUsername(refreshTokenRequest.refreshToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshTokenRequest.refreshToken(), user.getEmail())) {
            throw new RuntimeException("Invalid refresh token");
        }

        String refreshToken = jwtService.generateAccessToken(user.getEmail());

        return new RefreshTokenResponse(
                refreshToken,
                jwtService.getAccessTokenInSeconds()
        );
    }
}
