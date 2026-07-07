package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.StatutSinistre;
import java.time.LocalDate;

public record SinistreResponseDto(
        Long id,
        Long contratId,
        String description,
        LocalDate dateSinistre,
        LocalDate dateDeclaration,
        StatutSinistre statut
) {}