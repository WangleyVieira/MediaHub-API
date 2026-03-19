package com.mediahub.mediahub_api.dto;

public record RefreshTokenResponse(
        String refreshToken,
        long expiresIn
) {}
