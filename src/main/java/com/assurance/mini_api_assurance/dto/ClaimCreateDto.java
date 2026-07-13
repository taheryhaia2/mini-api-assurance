package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimCreateDto(
        @NotBlank String description,
        @NotNull LocalDate claimDate,
        @NotNull @Positive BigDecimal estimatedAmount
) {}