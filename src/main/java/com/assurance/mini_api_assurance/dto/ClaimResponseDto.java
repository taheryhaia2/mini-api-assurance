package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimResponseDto(
        Long id,
        String claimNumber,
        Long contractId,
        String description,
        LocalDate claimDate,
        LocalDate declarationDate,
        BigDecimal estimatedAmount,
        BigDecimal reimbursedAmount,
        ClaimStatus status
) {}