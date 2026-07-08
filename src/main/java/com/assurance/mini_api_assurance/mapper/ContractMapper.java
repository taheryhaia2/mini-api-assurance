package com.assurance.mini_api_assurance.mapper;

import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.dto.ContractCreateDto;
import com.assurance.mini_api_assurance.dto.ContractResponseDto;

public class ContractMapper {
    public static Contract toEntity(ContractCreateDto dto) {
        Contract contract = new Contract();
        contract.setStartDate(dto.startDate());
        contract.setEndDate(dto.endDate());
        contract.setCoverageAmount(dto.coverageAmount());
        // The Service will attach the Client
        return contract;
    }

    public static ContractResponseDto toDto(Contract contract) {
        return new ContractResponseDto(
                contract.getId(),
                contract.getClient().getId(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getStatus(),
                contract.getCoverageAmount()
        );
    }
}
