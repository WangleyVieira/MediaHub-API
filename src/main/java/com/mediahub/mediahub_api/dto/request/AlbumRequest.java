package com.mediahub.mediahub_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record AlbumRequest(

        @NotBlank(message = "Title is required")
        String title,

        @NotNull(message = "Data de lançamento é obrigatória")
        LocalDate releaseDate,

        Set<UUID> userIds
) {}
