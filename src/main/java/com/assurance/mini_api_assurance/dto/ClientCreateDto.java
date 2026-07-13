package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientCreateDto(
        @NotBlank String lastName,
        @NotBlank String firstName,
        @Email @NotBlank String email,
        @NotBlank String cin,
        @NotBlank String phoneNumber,   // NEW
        @NotBlank String address,       // NEW
        @NotNull LocalDate birthDate
) {}