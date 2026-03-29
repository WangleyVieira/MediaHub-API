package com.mediahub.mediahub_api.dto.response;

import java.util.UUID;

public record UserAlbumResponse(
        UUID id,
        String name
) {}
