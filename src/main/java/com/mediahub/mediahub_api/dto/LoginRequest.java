package com.mediahub.mediahub_api.dto;

public record LoginRequest(
        String email,
        String password
) {}
