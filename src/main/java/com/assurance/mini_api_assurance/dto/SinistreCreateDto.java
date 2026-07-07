package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SinistreCreateDto(
        @NotBlank(message = "La description du sinistre est obligatoire")
        String description,

        @NotNull(message = "La date du sinistre est obligatoire")
        LocalDate dateSinistre
) {}