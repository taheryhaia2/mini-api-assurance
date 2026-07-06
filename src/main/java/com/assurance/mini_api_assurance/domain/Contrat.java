package com.assurance.mini_api_assurance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutContrat statut;

    private BigDecimal montantCouverture;

    // Getters et Setters (Génère-les avec Alt+Insert)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public StatutContrat getStatut() { return statut; }
    public void setStatut(StatutContrat statut) { this.statut = statut; }

    public BigDecimal getMontantCouverture() { return montantCouverture; }
    public void setMontantCouverture(BigDecimal montantCouverture) { this.montantCouverture = montantCouverture; }
}