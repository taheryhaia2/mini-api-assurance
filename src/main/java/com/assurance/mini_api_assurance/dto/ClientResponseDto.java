package com.assurance.mini_api_assurance.dto;

import java.time.LocalDate;

public record ClientResponseDto(
        Long id,
        String lastName,
        String firstName,
        String email,
        String cin,
        String phoneNumber,   // NEW
        String address,       // NEW
        LocalDate birthDate,
        LocalDate createdAt
) {}