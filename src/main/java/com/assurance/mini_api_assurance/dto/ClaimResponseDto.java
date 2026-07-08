package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.ClaimStatus;
import java.time.LocalDate;

public record ClaimResponseDto(
        Long id,
        Long contractId,
        String description,
        LocalDate claimDate,
        LocalDate declarationDate,
        ClaimStatus status
) {}
