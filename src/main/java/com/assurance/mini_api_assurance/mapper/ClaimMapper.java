package com.assurance.mini_api_assurance.mapper;

import com.assurance.mini_api_assurance.domain.Claim;
import com.assurance.mini_api_assurance.dto.ClaimResponseDto;

public class ClaimMapper {

    public static ClaimResponseDto toDto(Claim claim) {
        return new ClaimResponseDto(
                claim.getId(),
                claim.getClaimNumber(),
                claim.getContract().getId(),
                claim.getDescription(),
                claim.getClaimDate(),
                claim.getDeclarationDate(),
                claim.getEstimatedAmount(),
                claim.getReimbursedAmount(),
                claim.getStatus()
        );
    }
}