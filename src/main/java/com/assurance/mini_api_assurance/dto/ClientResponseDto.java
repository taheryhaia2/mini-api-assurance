package com.assurance.mini_api_assurance.dto;

import java.time.LocalDate;

public record ClientResponseDto(
        Long id,
        String nom,
        String prenom,
        String email,
        String cin,
        LocalDate dateNaissance,
        LocalDate dateCreation
) {}