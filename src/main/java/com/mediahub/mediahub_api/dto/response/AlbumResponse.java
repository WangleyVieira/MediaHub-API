package com.mediahub.mediahub_api.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record AlbumResponse(
        long id,
        String title,
        LocalDate releaseDate,
        Set<UserResumeResponse> userIds
) {}
