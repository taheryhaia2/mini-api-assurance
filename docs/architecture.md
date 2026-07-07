# Architecture Mini-API Assurance

## Diagramme de classes (Modèle de domaine)

```mermaid
classDiagram
    class Client {
        -Long id
        -String nom
        -String prenom
        -String email
        -String cin
        -LocalDate dateNaissance
        -LocalDate dateCreation
    }

    class Contrat {
        -Long id
        -LocalDate dateDebut
        -LocalDate dateFin
        -Double montantCouverture
        -StatutContrat statut
    }

    class Sinistre {
        -Long id
        -String description
        -LocalDate dateSinistre
        -LocalDate dateDeclaration
        -StatutSinistre statut
    }

    class StatutContrat {
        <<enumeration>>
        ACTIF
        EXPIRE
        RESILIE
    }

    class StatutSinistre {
        <<enumeration>>
        DECLARE
        EN_COURS
        CLOTURE
    }

    Client "1" -- "*" Contrat : possede
    Contrat "1" -- "*" Sinistre : contient
    Contrat --> StatutContrat : a pour statut
    Sinistre --> StatutSinistre : a pour statut