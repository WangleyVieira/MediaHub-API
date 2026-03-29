package com.mediahub.mediahub_api.dto.response;

public record AuthResponse(
        String acessToken,
        String refreshToken,
        long expiresIn,
        UserResponse userResponse
) {}
