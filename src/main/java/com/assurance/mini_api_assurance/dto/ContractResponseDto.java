package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.ContractStatus;
import com.assurance.mini_api_assurance.domain.ContractType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractResponseDto(
        Long id,
        String policyNumber,
        Long clientId,
        ContractType type,
        LocalDate startDate,
        LocalDate endDate,
        ContractStatus status,
        BigDecimal coverageAmount,
        BigDecimal premiumAmount
) {}