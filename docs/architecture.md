# Architecture Mini-API Assurance

## Domain Model Class Diagram

```mermaid
classDiagram
    class Client {
        -Long id
        -String lastName
        -String firstName
        -String email
        -String cin
        -LocalDate birthDate
        -LocalDate createdAt
    }

    class Contract {
        -Long id
        -LocalDate startDate
        -LocalDate endDate
        -BigDecimal coverageAmount
        -ContractStatus status
    }

    class Claim {
        -Long id
        -String description
        -LocalDate claimDate
        -LocalDate declarationDate
        -ClaimStatus status
    }

    class ContractStatus {
        <<enumeration>>
        ACTIVE
        EXPIRED
        TERMINATED
    }

    class ClaimStatus {
        <<enumeration>>
        SUBMITTED
        PROCESSING
        CLOSED
    }

    Client "1" -- "*" Contract : owns
    Contract "1" -- "*" Claim : contains
    Contract --> ContractStatus : has status
    Claim --> ClaimStatus : has status
```

## Translation Notes

- `Contrat` → `Contract`
- `Sinistre` → `Claim`
- `StatutContrat` → `ContractStatus` : ACTIF/EXPIRE/RESILIE → ACTIVE/EXPIRED/TERMINATED
- `StatutSinistre` → `ClaimStatus` : DECLARE/EN_COURS/CLOTURE → SUBMITTED/PROCESSING/CLOSED
- Field mapping preserved 1:1, JPA constraints preserved (`@Column(unique=true, nullable=false)`, `@Column(nullable=false)`, `@GeneratedValue`).
```
