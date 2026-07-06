package com.assurance.mini_api_assurance.mapper;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;

public class ClientMapper {

    // Transforme le DTO d'entrée en Entité JPA pour la sauvegarde
    public static Client toEntity(ClientCreateDto dto) {
        Client client = new Client();
        client.setNom(dto.nom());
        client.setPrenom(dto.prenom());
        client.setEmail(dto.email());
        client.setCin(dto.cin());
        client.setDateNaissance(dto.dateNaissance());
        return client;
    }

    // Transforme l'Entité JPA en DTO de sortie pour la réponse HTTP
    public static ClientResponseDto toDto(Client client) {
        return new ClientResponseDto(
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getCin(),
                client.getDateNaissance(),
                client.getDateCreation()
        );
    }
}