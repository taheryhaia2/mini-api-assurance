package com.assurance.mini_api_assurance.controller;

import com.assurance.mini_api_assurance.dto.ContractCreateDto;
import com.assurance.mini_api_assurance.dto.ContractResponseDto;
import com.assurance.mini_api_assurance.dto.ContractUpdateDto;
import com.assurance.mini_api_assurance.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<ContractResponseDto> createContract(@Valid @RequestBody ContractCreateDto dto) {
        ContractResponseDto response = contractService.createContract(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ContractResponseDto>> listContracts() {
        return ResponseEntity.ok(contractService.listContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDto> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContractResponseDto> updateContract(@PathVariable Long id, @Valid @RequestBody ContractUpdateDto dto) {
        ContractResponseDto updatedContract = contractService.updateContract(id, dto);
        return ResponseEntity.ok(updatedContract);
    }
}
