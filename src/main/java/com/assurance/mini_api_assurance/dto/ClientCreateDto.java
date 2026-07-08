package com.assurance.mini_api_assurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ClientCreateDto(
        @NotBlank
        String lastName,
        @NotBlank
        String firstName,
        @NotBlank @Email
        String email,
        @NotBlank
        String cin,
        LocalDate birthDate
) {}
