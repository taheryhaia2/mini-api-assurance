package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record ClientCreateDto(
        @NotBlank String lastName,
        @NotBlank String firstName,
        @Email @NotBlank String email,
        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "Le CIN doit contenir exactement 8 chiffres")
        String cin,
        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "Le téléphone doit contenir exactement 8 chiffres")
        @NotBlank String phoneNumber,
        @NotBlank String address,
        @NotNull LocalDate birthDate
) {}