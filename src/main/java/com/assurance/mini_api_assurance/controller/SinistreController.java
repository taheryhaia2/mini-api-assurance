package com.assurance.mini_api_assurance.controller;

import com.assurance.mini_api_assurance.domain.Sinistre;
import com.assurance.mini_api_assurance.dto.SinistreCreateDto;
import com.assurance.mini_api_assurance.dto.SinistreResponseDto;
import com.assurance.mini_api_assurance.service.SinistreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.assurance.mini_api_assurance.service.SinistreService;
@RestController
@RequestMapping("/api")
public class SinistreController {
    private final SinistreService sinistreService;
    public SinistreController(SinistreService sinistreService){
        this.sinistreService=sinistreService;
    }
    @PostMapping("/contrats/{contratId}/sinistres")
    public ResponseEntity<SinistreResponseDto> declarerSinistre(
            @PathVariable Long contratId,
            @Valid @RequestBody SinistreCreateDto dto) {

        SinistreResponseDto sinistreCree = sinistreService.creerSinistre(contratId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(sinistreCree);
    }
}
