package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.StatutContrat;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratResponseDto(
        Long id,
        Long clientId,
        LocalDate dateDebut,
        LocalDate dateFin,
        StatutContrat statut,
        BigDecimal montantCouverture
) {}