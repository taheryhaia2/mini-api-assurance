package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.domain.ContractStatus; // adapte si ton enum est dans domain.enums
import com.assurance.mini_api_assurance.dto.ClaimCreateDto;
import com.assurance.mini_api_assurance.exception.BusinessRuleException;
import com.assurance.mini_api_assurance.repository.ClaimRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ClaimService claimService;

    @Test
    void createClaim_shouldThrowBusinessRuleException_whenContractIsTerminated() {
        // Arrange
        Long contractId = 1L;

        Contract terminatedContract = new Contract();
        terminatedContract.setId(contractId);
        terminatedContract.setStatus(ContractStatus.TERMINATED);

        when(contractRepository.findById(contractId))
                .thenReturn(Optional.of(terminatedContract));

        ClaimCreateDto dto = new ClaimCreateDto(
                "Accident test",
                LocalDate.now(),
                new BigDecimal("1500.00")
        );

        // Act + Assert
        assertThrows(BusinessRuleException.class, () ->
                claimService.createClaim(contractId, dto)
        );
    }
}