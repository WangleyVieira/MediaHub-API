package com.mediahub.mediahub_api.dto;

public record AuthResponse(
        String acessToken,
        String refreshToken,
        long expiresIn,
        UserResponse userResponse
) {}
