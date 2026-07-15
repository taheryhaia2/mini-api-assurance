package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractUpdateDto(
        @Positive(message = "Coverage amount must be positive") BigDecimal coverageAmount,
        @Positive(message = "Premium amount must be positive") BigDecimal premiumAmount,
        LocalDate endDate,
        String status
) {}