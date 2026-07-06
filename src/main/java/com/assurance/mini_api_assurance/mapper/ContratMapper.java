package com.assurance.mini_api_assurance.mapper;

import com.assurance.mini_api_assurance.domain.Contrat;
import com.assurance.mini_api_assurance.dto.ContratCreateDto;
import com.assurance.mini_api_assurance.dto.ContratResponseDto;

public class ContratMapper {
    public static Contrat toEntity(ContratCreateDto dto){
        Contrat contrat = new Contrat();
        contrat.setDateDebut(dto.dateDebut());
        contrat.setDateFin(dto.dateFin());
        contrat.setMontantCouverture(dto.montantCouverture());
        // Le Service s'occupera d'attacher le Client
        return contrat;
    }
    public static ContratResponseDto toDto(Contrat contrat) {
        return new ContratResponseDto(
                contrat.getId(),
                contrat.getClient().getId(), // On extrait juste l'ID du client
                contrat.getDateDebut(),
                contrat.getDateFin(),
                contrat.getStatut(),
                contrat.getMontantCouverture()
        );
    }

}
