package com.mediahub.mediahub_api.dto.response;

import java.util.UUID;

public record UserResumeResponse(
        UUID id,
        String name
) {}
