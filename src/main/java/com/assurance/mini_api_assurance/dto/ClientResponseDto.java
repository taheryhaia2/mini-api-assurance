package com.assurance.mini_api_assurance.dto;

import java.time.LocalDate;

public record ClientResponseDto(
        Long id,
        String lastName,
        String firstName,
        String email,
        String cin,
        LocalDate birthDate,
        LocalDate createdAt
) {}
