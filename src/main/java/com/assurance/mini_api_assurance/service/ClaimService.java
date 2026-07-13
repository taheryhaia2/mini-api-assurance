package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Claim;
import com.assurance.mini_api_assurance.domain.ClaimStatus;
import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.domain.ContractStatus;
import com.assurance.mini_api_assurance.dto.ClaimCreateDto;
import com.assurance.mini_api_assurance.dto.ClaimResponseDto;
import com.assurance.mini_api_assurance.exception.BusinessRuleException;
import com.assurance.mini_api_assurance.exception.NotFoundException;
import com.assurance.mini_api_assurance.mapper.ClaimMapper;
import com.assurance.mini_api_assurance.repository.ClaimRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ContractRepository contractRepository;

    public ClaimService(ClaimRepository claimRepository, ContractRepository contractRepository) {
        this.claimRepository = claimRepository;
        this.contractRepository = contractRepository;
    }

    @Transactional
    public ClaimResponseDto createClaim(Long contractId, ClaimCreateDto dto) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new NotFoundException("Contract not found with ID: " + contractId));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new BusinessRuleException("Cannot file a claim: Contract is not ACTIVE");
        }

        if (dto.claimDate().isBefore(contract.getStartDate()) || dto.claimDate().isAfter(contract.getEndDate())) {
            throw new BusinessRuleException("Claim date must be within the contract coverage period");
        }

        Claim claim = new Claim();
        claim.setContract(contract);
        claim.setDescription(dto.description());
        claim.setClaimDate(dto.claimDate());
        claim.setDeclarationDate(LocalDate.now());
        claim.setEstimatedAmount(dto.estimatedAmount());
        claim.setReimbursedAmount(BigDecimal.ZERO); // Initialisé à 0
        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setClaimNumber(generateClaimNumber());

        Claim saved = claimRepository.save(claim);
        return ClaimMapper.toDto(saved);
    }

    private String generateClaimNumber() {
        // Exemple: CL-2026-83421
        return "CL-" + Year.now().getValue() + "-" + (System.currentTimeMillis() % 100000);
    }
}