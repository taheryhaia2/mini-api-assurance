package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Contrat;
import com.assurance.mini_api_assurance.domain.Sinistre;
import com.assurance.mini_api_assurance.domain.StatutContrat;
import com.assurance.mini_api_assurance.domain.StatutSinistre;
import com.assurance.mini_api_assurance.dto.SinistreCreateDto;
import com.assurance.mini_api_assurance.dto.SinistreResponseDto;
import com.assurance.mini_api_assurance.repository.ContratRepository;
import com.assurance.mini_api_assurance.repository.SinistreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SinistreService {
    private final SinistreRepository sinistreRepository;
    private final ContratRepository contratRepository;
    public SinistreService(SinistreRepository sinistreRepository, ContratRepository contratRepository){
        this.sinistreRepository=sinistreRepository;
        this.contratRepository=contratRepository;
    }
    @Transactional
    public SinistreResponseDto creerSinistre(Long contratId, SinistreCreateDto dto){
        //Recuperer le contrat
        Contrat contrat=contratRepository.findById(contratId).orElseThrow(()->new RuntimeException("Contrat non trouvé"));
        //regles metiers
        //regle metier1:verifier le statut de contrat
        if(contrat.getStatut()!= StatutContrat.ACTIF){
            throw new RuntimeException("Impossible de déclarer un sinistre : le contrat n'est pas ACTIF");
        }
        //regle metier2:verifier la coherence den dates
        if(dto.dateSinistre().isBefore(contrat.getDateDebut())||dto.dateSinistre().isAfter(contrat.getDateFin())){
            throw new RuntimeException("La date du sinistre doit être pendant la période de validité du contrat");
        }
        //creation du sinistre
        Sinistre sinistre=new Sinistre();
        sinistre.setDateSinistre(dto.dateSinistre());
        sinistre.setContrat(contrat);
        sinistre.setDescription(dto.description());
        sinistre.setStatut(StatutSinistre.DECLARE);//forcé
        sinistre.setDateDeclaration(LocalDate.now());//forcé
        // 4. Sauvegarde en base
        Sinistre savedSinistre = sinistreRepository.save(sinistre);
        return new SinistreResponseDto(
                savedSinistre.getId(),
                savedSinistre.getContrat().getId(),
                savedSinistre.getDescription(),
                savedSinistre.getDateSinistre(),
                savedSinistre.getDateDeclaration(),
                savedSinistre.getStatut()
        );



    }
}
