package com.mediahub.mediahub_api.dto;

public record AuthResponse(
        String acessToken,
        long expiresIn,
        UserResponse userResponse
) {}
