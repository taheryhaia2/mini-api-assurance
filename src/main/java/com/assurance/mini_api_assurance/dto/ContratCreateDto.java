package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratCreateDto(
        @NotNull Long clientId,
        @NotNull LocalDate dateDebut,
        @NotNull LocalDate dateFin,
        @NotNull @DecimalMin(value = "0.0", inclusive = true, message = "Le montant de couverture ne peut pas être négatif") BigDecimal montantCouverture
) {}