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
        -String phoneNumber
        -String address
        -LocalDate birthDate
        -LocalDate createdAt
    }

    class Contract {
        -Long id
        -String policyNumber
        -ContractType type
        -LocalDate startDate
        -LocalDate endDate
        -BigDecimal coverageAmount
        -BigDecimal premiumAmount
        -ContractStatus status
    }

    class Claim {
        -Long id
        -String claimNumber
        -String description
        -LocalDate claimDate
        -LocalDate declarationDate
        -BigDecimal estimatedAmount
        -BigDecimal reimbursedAmount
        -ClaimStatus status
    }

    class User {
        -Long id
        -String username
        -String password
        -Role role
    }

    class ContractType {
        <<enumeration>>
        AUTO
        HOME
        HEALTH
        LIFE
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
        ACCEPTED
        REJECTED
    }

    class Role {
        <<enumeration>>
        ADMIN
        AGENT
    }

    Client "1" -- "*" Contract : owns
    Contract "1" -- "*" Claim : contains
    User --> Role : has role
    Contract --> ContractType : has type
    Contract --> ContractStatus : has status
    Claim --> ClaimStatus : has status