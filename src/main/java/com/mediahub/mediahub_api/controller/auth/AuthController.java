package com.mediahub.mediahub_api.controller.auth;

import com.mediahub.mediahub_api.dto.AuthResponse;
import com.mediahub.mediahub_api.dto.LoginRequest;
import com.mediahub.mediahub_api.dto.RefreshTokenRequest;
import com.mediahub.mediahub_api.dto.RefreshTokenResponse;
import com.mediahub.mediahub_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
}
