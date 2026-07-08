package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.ContractStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractResponseDto(
        Long id,
        Long clientId,
        LocalDate startDate,
        LocalDate endDate,
        ContractStatus status,
        BigDecimal coverageAmount
) {}
