package com.assurance.mini_api_assurance.mapper;

import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.dto.ContractResponseDto;

public class ContractMapper {

    public static ContractResponseDto toDto(Contract contract) {
        return new ContractResponseDto(
                contract.getId(),
                contract.getPolicyNumber(),
                contract.getClient().getId(),
                contract.getType(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getStatus(),
                contract.getCoverageAmount(),
                contract.getPremiumAmount()
        );
    }
}