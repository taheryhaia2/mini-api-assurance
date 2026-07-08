package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractCreateDto(
        @NotNull
        Long clientId,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull @DecimalMin(value = "0.0", inclusive = true, message = "Coverage amount cannot be negative")
        BigDecimal coverageAmount
) {}
