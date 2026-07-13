package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.ContractType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractCreateDto(
        @NotNull Long clientId,
        @NotNull ContractType type,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @Positive BigDecimal coverageAmount,
        @NotNull @Positive BigDecimal premiumAmount
) {}