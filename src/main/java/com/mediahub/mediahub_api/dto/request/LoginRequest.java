package com.mediahub.mediahub_api.dto.request;

public record LoginRequest(
        String email,
        String password
) {}
