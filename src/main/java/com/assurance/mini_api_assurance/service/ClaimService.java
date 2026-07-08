package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Claim;
import com.assurance.mini_api_assurance.domain.ClaimStatus;
import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.domain.ContractStatus;
import com.assurance.mini_api_assurance.dto.ClaimCreateDto;
import com.assurance.mini_api_assurance.dto.ClaimResponseDto;
import com.assurance.mini_api_assurance.repository.ClaimRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
        // Retrieve the contract
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        // Business rule 1: check contract status
        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new RuntimeException("Cannot file a claim: the contract is not ACTIVE");
        }

        // Business rule 2: check date consistency
        if (dto.claimDate().isBefore(contract.getStartDate()) || dto.claimDate().isAfter(contract.getEndDate())) {
            throw new RuntimeException("Claim date must be within the contract validity period");
        }

        // Create claim
        Claim claim = new Claim();
        claim.setClaimDate(dto.claimDate());
        claim.setContract(contract);
        claim.setDescription(dto.description());
        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setDeclarationDate(LocalDate.now());

        // Save to database
        Claim savedClaim = claimRepository.save(claim);
        return new ClaimResponseDto(
                savedClaim.getId(),
                savedClaim.getContract().getId(),
                savedClaim.getDescription(),
                savedClaim.getClaimDate(),
                savedClaim.getDeclarationDate(),
                savedClaim.getStatus()
        );
    }
}
