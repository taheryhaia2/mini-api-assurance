package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank(message = "Le username est obligatoire")
        String username,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password,

        @NotNull(message = "Le rôle est obligatoire")
        Role role
) {}