package com.assurance.mini_api_assurance.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Sinistre {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrat_id",nullable=false)
    private Contrat contrat;
    private LocalDate dateSinistre;
    private LocalDate dateDeclaration;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSinistre statut;
    private String description;
    // Getters et Setters (Génère avec Alt+Insert)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Contrat getContrat() { return contrat; }
    public void setContrat(Contrat contrat) { this.contrat = contrat; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateSinistre() { return dateSinistre; }
    public void setDateSinistre(LocalDate dateSinistre) { this.dateSinistre = dateSinistre; }

    public LocalDate getDateDeclaration() { return dateDeclaration; }
    public void setDateDeclaration(LocalDate dateDeclaration) { this.dateDeclaration = dateDeclaration; }

    public StatutSinistre getStatut() { return statut; }
    public void setStatut(StatutSinistre statut) { this.statut = statut; }

}
