# Mini-API Assurance

API REST de gestion de contrats d'assurance développée avec Spring Boot dans le cadre d'un stage d'été.

## Fonctionnalités

- Gestion des Clients (Création, Lecture)
- Gestion des Contrats (Création, Lecture, Règles métier de validation des dates)
- Gestion des Sinistres (Création, Règle métier : dépôt impossible sur un contrat inactif ou hors période de validité)
- Sécurité JWT (Authentification par Token, Inscription, Login)
- Documentation interactive Swagger/OpenAPI

## Prérequis

- Java 17
- Maven 3.8+
- PostgreSQL

## Installation et Lancement

1. **Cloner le repository**
   ```bash
   git clone https://github.com/taheryhaia2/mini-api-assurance.git
   cd mini-api-assurance
Configurer la base de données

Créer une base de données PostgreSQL nommée assurance_db.
Modifier les identifiants dans src/main/resources/application.yml si nécessaire (par défaut : user=postgres, password=admin).
Lancer l'application

Bash

./mvnw spring-boot:run
Accéder à la documentation Swagger
L'interface interactive est disponible à l'adresse :
👉 http://localhost:8080/swagger-ui/index.html

Utilisation de l'API (Workflow)
Inscription : Utiliser POST /api/auth/register pour créer un compte (ex: ADMIN).
Connexion : Utiliser POST /api/auth/login pour obtenir un Token JWT.
Autorisation : Cliquer sur le bouton 🔒 Authorize dans Swagger, saisir Bearer <votre_token> pour accéder aux routes protégées.
Opérations : Créer des clients, des contrats et déclarer des sinistres.
Architecture
Framework : Spring Boot 3
Base de données : PostgreSQL avec Spring Data JPA (Hibernate)
Sécurité : Spring Security + JWT (JJWT)
Documentation : Springdoc OpenAPI (Swagger)