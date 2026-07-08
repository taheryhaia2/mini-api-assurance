package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ClaimCreateDto(
        @NotBlank(message = "Claim description is required")
        String description,

        @NotNull(message = "Claim date is required")
        LocalDate claimDate
) {}
