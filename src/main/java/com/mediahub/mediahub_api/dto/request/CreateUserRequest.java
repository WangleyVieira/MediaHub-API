package com.mediahub.mediahub_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve conter no máximo 100 caracteres")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Size(max = 150, message = "Email deve conter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {}
