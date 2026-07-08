package com.assurance.mini_api_assurance.mapper;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;

public class ClientMapper {

    // Transforms input DTO into JPA Entity for persistence
    public static Client toEntity(ClientCreateDto dto) {
        Client client = new Client();
        client.setLastName(dto.lastName());
        client.setFirstName(dto.firstName());
        client.setEmail(dto.email());
        client.setCin(dto.cin());
        client.setBirthDate(dto.birthDate());
        return client;
    }

    // Transforms JPA Entity into output DTO for HTTP response
    public static ClientResponseDto toDto(Client client) {
        return new ClientResponseDto(
                client.getId(),
                client.getLastName(),
                client.getFirstName(),
                client.getEmail(),
                client.getCin(),
                client.getBirthDate(),
                client.getCreatedAt()
        );
    }
}
