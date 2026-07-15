package com.assurance.mini_api_assurance.controller;

import com.assurance.mini_api_assurance.dto.ClaimCreateDto;
import com.assurance.mini_api_assurance.dto.ClaimResponseDto;
import com.assurance.mini_api_assurance.dto.ContractResponseDto;
import com.assurance.mini_api_assurance.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/contracts/{contractId}/claims")
    public ResponseEntity<ClaimResponseDto> createClaim(
            @PathVariable Long contractId,
            @Valid @RequestBody ClaimCreateDto dto) {

        ClaimResponseDto createdClaim = claimService.createClaim(contractId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdClaim);
    }
    @GetMapping("/contracts/{contractId}/claims")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<ClaimResponseDto>> getClaimsByContractId(@PathVariable Long contractId) {
        List<ClaimResponseDto> claims = claimService.getClaimsByContractId(contractId);
        return ResponseEntity.ok(claims);
    }
}
