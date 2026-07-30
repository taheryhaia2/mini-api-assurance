package com.assurance.mini_api_assurance.dto;

import com.assurance.mini_api_assurance.domain.Role;

public record UserResponseDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        Role role
) {}