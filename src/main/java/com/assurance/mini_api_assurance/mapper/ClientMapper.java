package com.assurance.mini_api_assurance.mapper;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;
import com.assurance.mini_api_assurance.dto.ClientUpdateDto;

public class ClientMapper {

    public static Client toEntity(ClientCreateDto dto) {
        Client client = new Client();
        client.setLastName(dto.lastName());
        client.setFirstName(dto.firstName());
        client.setEmail(dto.email());
        client.setCin(dto.cin());
        client.setPhoneNumber(dto.phoneNumber());
        client.setAddress(dto.address());
        client.setBirthDate(dto.birthDate());
        return client;
    }

    public static ClientResponseDto toDto(Client client) {
        return new ClientResponseDto(
                client.getId(),
                client.getLastName(),
                client.getFirstName(),
                client.getEmail(),
                client.getCin(),
                client.getPhoneNumber(),
                client.getAddress(),
                client.getBirthDate(),
                client.getCreatedAt()
        );
    }

    public static void updateEntity(Client client, ClientUpdateDto dto) {
        client.setLastName(dto.lastName());
        client.setFirstName(dto.firstName());
        client.setEmail(dto.email());
        client.setPhoneNumber(dto.phoneNumber());
        client.setAddress(dto.address());
        client.setBirthDate(dto.birthDate());
    }
}