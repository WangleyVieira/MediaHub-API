package com.mediahub.mediahub_api.dto.response;

public record RefreshTokenResponse(
        String refreshToken,
        long expiresIn
) {}
