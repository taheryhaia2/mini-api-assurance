package com.assurance.mini_api_assurance.controller;

import com.assurance.mini_api_assurance.dto.ContratCreateDto;
import com.assurance.mini_api_assurance.dto.ContratResponseDto;
import com.assurance.mini_api_assurance.service.ContratService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contrats")
public class ContratController {

    private final ContratService contratService;

    public ContratController(ContratService contratService) {
        this.contratService = contratService;
    }

    @PostMapping
    public ResponseEntity<ContratResponseDto> creerContrat(@Valid @RequestBody ContratCreateDto dto) {
        ContratResponseDto response = contratService.creerContrat(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}