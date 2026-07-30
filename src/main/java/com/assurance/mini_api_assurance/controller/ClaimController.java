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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping(value = "/contracts/{contractId}/claims", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClaimResponseDto> createClaim(
            @PathVariable Long contractId,
            @ModelAttribute ClaimCreateDto dto, // <-- Changé de @RequestBody à @ModelAttribute
            @RequestParam(value = "file", required = false) MultipartFile file) { // <-- Le fichier

        // On passe le contrat, le dto ET le fichier au service
        ClaimResponseDto createdClaim = claimService.createClaim(contractId, dto, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdClaim);
    }
    @GetMapping("/contracts/{contractId}/claims")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<ClaimResponseDto>> getClaimsByContractId(@PathVariable Long contractId) {
        List<ClaimResponseDto> claims = claimService.getClaimsByContractId(contractId);
        return ResponseEntity.ok(claims);
    }
    @GetMapping("/claims")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<ClaimResponseDto>> getAllClaims() {
        List<ClaimResponseDto> claims = claimService.getAllClaims();
        return ResponseEntity.ok(claims);
    }
    @GetMapping("/claims/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ClaimResponseDto> getClaimById(@PathVariable Long id) {
        ClaimResponseDto claim = claimService.getClaimById(id);
        return ResponseEntity.ok(claim);
    }

}
