# Inventaire factuel du dépôt `mini-api-assurance`

Document de travail préalable à la rédaction du rapport de stage.
Objectif : lister **uniquement** ce qui existe réellement dans le code, la configuration et la documentation du dépôt, afin que chaque affirmation du rapport soit vérifiable.

- Dépôt : `https://github.com/taheryhaia2/mini-api-assurance`
- Branche auditée : `arena/019fa2c0-mini-api-assurance` (dernier commit de stage : `d07e9e1`, 22/07/2026)
- Date de l'audit : 27/07/2026
- Volumétrie : ~1 578 lignes de Java (`src/`), ~524 lignes de TypeScript/HTML/CSS (`frontend/src/`)

## 0. Historique Git réel (29 commits, 8 journées)

L'historique complet a été récupéré (`git fetch --unshallow`). Il constitue la source du tableau 1 du rapport.

| Date | Commits | Contenu |
|---|---|---|
| 05/07 | 2 | Init Spring Initializr ; datasource PostgreSQL + `open-in-view: false` |
| 06/07 | 4 | Entité `Client` + repository ; CRUD complet (DTO/Service/Controller) ; GET list et by id ; entité `Contrat` + validation des dates |
| 07/07 | 4 | Entité `Sinistre` + règles + `GlobalExceptionHandler` ; diagramme Mermaid ; Swagger 2.7.0 ; fix `HashMap` vs `Map.of` |
| 08/07 | 4 | JWT + Spring Security ; README ; Swagger JWT + `DataInitializer` ; **traduction complète du code en anglais** (49 fichiers) |
| 13/07 | 5 | PUT client ; `phoneNumber`/`address` ; enrichissement `Client`/`Contract` ; enrichissement `Claim` ; mise à jour du diagramme |
| 15/07 | 4 | DELETE client + règle métier ; GET sinistres par contrat ; PUT contrat ; **unique test unitaire** |
| 21/07 | 1 | Fichiers Docker créés **vides** + `auth.model.ts` et `AuthService` |
| 22/07 | 5 | **Contenu réel des fichiers Docker** ; interceptor + guard ; liste clients ; lien navigation ; formulaire création ; CSS |

**Constats de méthode, à refléter honnêtement dans le rapport :**

- **Une seule branche** : `master` contient les 23 commits backend (jusqu'au 15/07). Les 6 commits frontend + Docker (21-22/07) sont sur la branche de travail. Aucune branche de fonctionnalité dédiée n'existe dans le dépôt distant — ne pas affirmer qu'une branche dédiée a été utilisée.
- **Commit du 21/07 non atomique** : il mélange l'ajout des trois fichiers Docker (créés vides, 0 ligne) et le début du service d'authentification Angular. Le contenu Docker n'arrive qu'au commit `04bf860` du 22/07, lui aussi mélangé avec l'interceptor et le guard.
- **Docker n'est donc pas un chantier de « semaine 4 » planifié** : c'est un ajout tardif, glissé dans des commits frontend.
- **Aucun développement entre le 8 et le 13 juillet, ni entre le 15 et le 21** : le travail s'est fait par journées espacées, non en flux continu sur 20 jours pleins.
- Le refactor du 08/07 (`9b10ff6`, 49 fichiers) a renommé tout le domaine du français vers l'anglais (`Contrat`→`Contract`, `Sinistre`→`Claim`).

---

## 1. Synthèse « réalisé / partiel / non réalisé »

| # | Élément | Statut | Preuve dans le dépôt |
|---|---|---|---|
| 1 | API REST Spring Boot 3.5.16 / Java 17, architecture Controller → Service → Repository | **Réalisé** | `pom.xml`, packages `controller`, `service`, `repository` |
| 2 | Modèle de domaine assurance (Client, Contract, Claim, User + 4 enums) persisté en PostgreSQL via JPA | **Réalisé** | `domain/*.java`, `application.yaml` |
| 3 | Authentification JWT (register, login, filtre, BCrypt, sessions stateless) | **Réalisé** | `security/*`, `config/SecurityConfig.java`, `controller/AuthController.java` |
| 4 | Documentation OpenAPI/Swagger avec schéma de sécurité `bearerAuth` | **Réalisé** | `config/OpenApiConfig.java`, dépendance `springdoc-openapi-starter-webmvc-ui:2.7.0` |
| 5 | Règles métier (dates de contrat, sinistre sur contrat non ACTIVE, sinistre hors période, suppression client lié à un contrat) | **Réalisé** | `service/ContractService.java`, `service/ClaimService.java`, `service/ClientService.java` |
| 6 | Conteneurisation API + base (Dockerfile multi-stage, `docker-compose.yml`, healthcheck, volume) | **Réalisé** | `Dockerfile`, `docker-compose.yml`, `.dockerignore` |
| 7 | Jeu de données de démonstration au démarrage (2 comptes, 1 client, 2 contrats) | **Réalisé** | `config/DataInitializer.java` |
| 8 | Autorisation par rôle (`@PreAuthorize`) | **Partiel / non effectif** | Annotations présentes sur 3 méthodes mais **`@EnableMethodSecurity` absent** du projet (`grep` négatif) → elles ne sont pas appliquées |
| 9 | Gestion centralisée des erreurs | **Partiel** | `GlobalExceptionHandler` existe mais renvoie **400 pour toute `RuntimeException`**, y compris `NotFoundException` (pas de 404, pas de 403/409) |
| 10 | Tests automatisés | **Partiel / minimal** | 2 classes : `MiniApiAssuranceApplicationTests` (`contextLoads`, nécessite une base) et `ClaimServiceTest` (**1 seul test unitaire Mockito**) |
| 11 | Frontend Angular 21 (login, dashboard, liste et création de clients, garde, intercepteur JWT) | **Partiel — initiative personnelle** | `frontend/src/app/**` |
| 12 | Écrans contrats / sinistres côté frontend | **Non réalisé** | Aucun composant, service ou route correspondants |
| 13 | Intégration continue (CI) | **Non réalisé** | Aucun dossier `.github/workflows` |
| 14 | Frontend conteneurisé | **Non réalisé** | `docker-compose.yml` ne déclare que `db` et `api` |
| 15 | Migrations de schéma versionnées (Flyway/Liquibase) | **Non réalisé** | `ddl-auto: update` (Hibernate) |

---

## 2. Backend — inventaire détaillé

### 2.1 Stack et versions (source : `pom.xml`)

| Composant | Version / portée |
|---|---|
| `spring-boot-starter-parent` | 3.5.16 |
| Java (`java.version`) | 17 |
| `spring-boot-starter-web` | héritée du parent |
| `spring-boot-starter-data-jpa` | héritée du parent |
| `spring-boot-starter-validation` | héritée du parent |
| `spring-boot-starter-security` | héritée du parent |
| `spring-boot-starter-test` | scope `test` |
| `postgresql` | scope `runtime`, version héritée |
| `springdoc-openapi-starter-webmvc-ui` | 2.7.0 |
| `jjwt-api` / `jjwt-impl` / `jjwt-jackson` | 0.12.6 |
| Build | `spring-boot-maven-plugin`, wrapper Maven (`mvnw`) |

Aucune dépendance Lombok, MapStruct, Flyway, Testcontainers ou Actuator.

### 2.2 Arborescence des packages (`com.assurance.mini_api_assurance`)

```
MiniApiAssuranceApplication.java
config/       DataInitializer, OpenApiConfig, SecurityConfig
controller/   AuthController, ClientController, ContractController, ClaimController
domain/       Client, Contract, Claim, User, ContractType, ContractStatus, ClaimStatus, Role
dto/          AuthResponse, LoginRequest, RegisterRequest,
              ClientCreateDto, ClientUpdateDto, ClientResponseDto,
              ContractCreateDto, ContractUpdateDto, ContractResponseDto,
              ClaimCreateDto, ClaimResponseDto
mapper/       ClientMapper, ContractMapper, ClaimMapper   (statiques, écrits à la main)
repository/   ClientRepository, ContractRepository, ClaimRepository, UserRepository
security/     JwtService, JwtAuthFilter, CustomUserDetails, CustomUserDetailsService
service/      ClientService, ContractService, ClaimService, UserService
exception/    NotFoundException, BusinessRuleException, GlobalExceptionHandler
```

### 2.3 Modèle de domaine

- **Client** : `id`, `lastName`, `firstName`, `email`, `cin` (unique), `phoneNumber`, `address`, `birthDate`, `createdAt`. Colonnes `nullable = false` sauf `birthDate` et `createdAt`.
- **Contract** : `id`, `policyNumber` (unique), `type` (enum `ContractType`), `client` (`@ManyToOne`, `LAZY`, `client_id` non nul), `startDate`, `endDate`, `status` (enum `ContractStatus`), `coverageAmount`, `premiumAmount` (`BigDecimal`).
- **Claim** : `id`, `claimNumber` (unique), `contract` (`@ManyToOne`, `LAZY`), `description`, `claimDate`, `declarationDate`, `estimatedAmount`, `reimbursedAmount`, `status` (enum `ClaimStatus`).
- **User** : table `app_user`, `id`, `username` (unique), `password` (haché BCrypt), `role` (enum `Role`).
- Enums : `ContractType {AUTO, HOME, HEALTH, LIFE}`, `ContractStatus {ACTIVE, EXPIRED, TERMINATED}`, `ClaimStatus {SUBMITTED, PROCESSING, ACCEPTED, REJECTED}`, `Role {ADMIN, AGENT}`.
- Associations **unidirectionnelles** (pas de `@OneToMany` côté `Client` ni `Contract`).

### 2.4 Endpoints réellement exposés

| Méthode | Route | Contrôleur | Sécurité effective | Code de retour nominal |
|---|---|---|---|---|
| POST | `/api/auth/register` | `AuthController` | publique | 201 |
| POST | `/api/auth/login` | `AuthController` | publique | 200 + token JWT |
| POST | `/api/clients` | `ClientController` | authentifié | 201 |
| GET | `/api/clients` | `ClientController` | authentifié | 200 |
| GET | `/api/clients/{id}` | `ClientController` | authentifié | 200 |
| PUT | `/api/clients/{id}` | `ClientController` | authentifié (`@PreAuthorize` commenté) | 200 |
| DELETE | `/api/clients/{id}` | `ClientController` | authentifié (`@PreAuthorize('ADMIN')` **inopérant**) | 204 |
| POST | `/api/contracts` | `ContractController` | authentifié | 201 |
| GET | `/api/contracts` | `ContractController` | authentifié | 200 |
| GET | `/api/contracts/{id}` | `ContractController` | authentifié | 200 |
| PUT | `/api/contracts/{id}` | `ContractController` | authentifié (`@PreAuthorize('ADMIN')` **inopérant**) | 200 |
| POST | `/api/contracts/{contractId}/claims` | `ClaimController` | authentifié | 201 |
| GET | `/api/contracts/{contractId}/claims` | `ClaimController` | authentifié (`@PreAuthorize` **inopérant**) | 200 |

Absents : suppression de contrat, suppression de sinistre, mise à jour de sinistre, liste globale des sinistres, pagination, filtres, recherche.

### 2.5 Règles métier codées

1. `ContractService.createContract` : `endDate` ne peut pas précéder `startDate` → `BusinessRuleException`.
2. `ContractService.createContract` : statut forcé à `ACTIVE`, `policyNumber` généré côté serveur (`CT-<année>-<millis % 100000>`).
3. `ContractService.updateContract` : conversion défensive de la chaîne `status` vers l'enum, sinon `BusinessRuleException`.
4. `ClaimService.createClaim` : refus si le contrat n'est pas `ACTIVE`.
5. `ClaimService.createClaim` : refus si `claimDate` hors de la période `[startDate, endDate]`.
6. `ClaimService.createClaim` : `status = SUBMITTED`, `reimbursedAmount = 0`, `declarationDate = now()`, `claimNumber` généré (`CL-<année>-<millis % 100000>`).
7. `ClientService.createClient` : `createdAt` imposé par le serveur.
8. `ClientService.deleteClient` : refus si des contrats sont rattachés au client.
9. `UserService.createUser` : refus si le `username` existe déjà.

### 2.6 Sécurité (état exact)

- `SecurityConfig` : CORS activé, **CSRF désactivé**, sessions `STATELESS`, `/api/auth/**` + `/swagger-ui/**` + `/v3/api-docs/**` publics, tout le reste authentifié, `JwtAuthFilter` placé avant `UsernamePasswordAuthenticationFilter`, `DaoAuthenticationProvider` + `BCryptPasswordEncoder`.
- CORS : origine autorisée **`http://localhost:4200` uniquement**, méthodes GET/POST/PUT/DELETE/OPTIONS, `allowCredentials(true)`.
- `JwtService` : signature HMAC via clé Base64, **expiration 24 h**, extraction du `subject`, validation nom d'utilisateur + expiration. Pas de `claims` de rôle dans le token, pas de refresh token.
- `JwtAuthFilter` : lit l'en-tête `Authorization: Bearer …`, recharge l'utilisateur depuis la base, peuple le `SecurityContext`. **Pas de `try/catch`** autour du parsing : un token expiré ou malformé remonte une exception.
- `CustomUserDetails` : autorité `ROLE_<ADMIN|AGENT>`; les quatre méthodes de statut de compte renvoient `true` (commentaires `TODO` pour une V2).

### 2.7 Validation et erreurs

- Bean Validation sur les DTO d'entrée : `@NotBlank`, `@NotNull`, `@Email`, `@Positive`, messages personnalisés sur `LoginRequest`, `RegisterRequest`, `ContractUpdateDto`.
- `GlobalExceptionHandler` : un seul `@ExceptionHandler(RuntimeException.class)` → `{"error": "..."}` avec statut **400 systématique**.
- Conséquence factuelle : `NotFoundException` renvoie 400 et non 404 ; une violation de contrainte SQL (CIN dupliqué) renvoie aussi 400 avec un message technique.

### 2.8 Configuration (`application.yaml`)

- `spring.datasource.*` surchargeables par variables d'environnement (`SPRING_DATASOURCE_URL`, `…_USERNAME`, `…_PASSWORD`), valeurs par défaut locales (`localhost:5432/assurance_db`, `postgres`/`admin`).
- `spring.jpa.hibernate.ddl-auto` : `update` (surchargeable).
- `show-sql: true`, `open-in-view: false`, `server.error.include-message: always`.
- `jwt.secret` : **valeur Base64 en dur, non externalisée** (pas de placeholder d'environnement).

### 2.9 Tests

| Classe | Type | Contenu |
|---|---|---|
| `MiniApiAssuranceApplicationTests` | `@SpringBootTest` | `contextLoads()` — nécessite une base PostgreSQL accessible |
| `ClaimServiceTest` | unitaire Mockito | `createClaim_shouldThrowBusinessRuleException_whenContractIsTerminated` |

Aucun test de contrôleur (`@WebMvcTest`), aucun test d'intégration de sécurité, aucun rapport de couverture. **Le rapport ne doit annoncer aucun pourcentage de couverture.**

---

## 3. Docker — inventaire détaillé

- **`Dockerfile`** : multi-stage. Étape 1 `maven:3.9.9-eclipse-temurin-17`, copie `pom.xml` puis `src`, `mvn -q -DskipTests package`. Étape 2 `eclipse-temurin:17-jre`, copie du jar, `EXPOSE 8080`, `ENTRYPOINT java -jar app.jar`.
- **`docker-compose.yml`** : service `db` (`postgres:16`, variables `POSTGRES_*`, port 5432 publié, volume nommé `pgdata`, healthcheck `pg_isready` toutes les 5 s, 10 tentatives) et service `api` (`build: .`, `depends_on: db condition: service_healthy`, variables `SPRING_DATASOURCE_*` et `SPRING_JPA_HIBERNATE_DDL_AUTO`, port 8080).
- **`.dockerignore`** : `target/`, `.git/`, `.idea/`, `*.iml`, `docs/`, `README.md`.
- **Limites objectives** : pas de cache des dépendances Maven (`dependency:go-offline` absent) → reconstruction longue ; `-DskipTests` dans l'image ; identifiants en clair dans le compose ; conteneur exécuté en `root` ; pas d'image du frontend ; pas de profil de production ; `jwt.secret` non injecté par variable d'environnement.

---

## 4. Frontend Angular — inventaire détaillé (initiative personnelle)

- Angular **21.2** (`@angular/core ^21.2.0`), CLI 21.2.19, TypeScript 5.9, Vitest 4 comme runner de test, composants **standalone**, bootstrap par `bootstrapApplication`.
- `app.config.ts` : `provideRouter(routes)`, `provideHttpClient(withInterceptors([jwtInterceptor]))`, `provideBrowserGlobalErrorListeners()`.
- Routes : `/login`, `/dashboard` (garde), `/clients` (garde), `/clients/new` (garde), redirection par défaut et wildcard vers `/login`.
- `AuthService` : `login()` (POST `/api/auth/login`), stockage du token et du `username` dans le **`localStorage`**, `logout()`, `getToken()`, `isLoggedIn()`, `getUsername()`.
- `jwtInterceptor` : ajoute `Authorization: Bearer <token>` si un token est présent. **Pas de gestion des réponses 401/403.**
- `authGuard` : redirige vers `/login` si aucun token. Contrôle limité à la **présence** du token (pas de vérification d'expiration).
- `ClientService` : `getAllClients()` et `createClient()` uniquement. URL de l'API **codée en dur** (`http://localhost:8080/...`, pas de fichier `environment`).
- Composants : `LoginComponent` (formulaire template-driven, message d'erreur générique), `DashboardComponent` (template inline, lien vers `/clients`, bouton de déconnexion), `ClientListComponent` (tableau, `signal<Client[]>`, syntaxe `@for`), `ClientFormComponent` (formulaire de création).
- **Non réalisé côté UI** : contrats, sinistres, modification/suppression de client, barre de navigation partagée, lien vers `/clients/new` depuis la liste, gestion d'expiration de session, tests (le spec généré `app.spec.ts` teste encore le gabarit par défaut « Hello, frontend » et n'est donc plus aligné sur le code).

---

## 5. Documentation existante et écarts constatés

| Source | Contenu | Écart avec le code |
|---|---|---|
| `README.md` | Présentation, prérequis, démarrage, workflow Swagger, comptes de démo | Annonce « Client Management (Create, Read) » alors que **update et delete existent** ; renvoie à `application.yml` alors que le fichier est `application.yaml` ; ne mentionne **ni Docker ni le frontend Angular** |
| `docs/architecture.md` | Diagramme de classes Mermaid du domaine | Cohérent avec le code ; le bloc Mermaid n'est **pas refermé** (``` manquant) et le diagramme ne montre pas les couches applicatives |
| `frontend/README.md` | README généré par Angular CLI, non personnalisé | Aucune information projet |

**Actions recommandées à l'étudiant (hors rapport)** : compléter le `README` (Docker, frontend), refermer le bloc Mermaid de `docs/architecture.md`.

---

## 6. Figures et tableaux retenus pour le rapport

**Figures**
1. Figure 1 — Architecture globale en couches (client HTTP → contrôleur → service → repository → PostgreSQL).
2. Figure 2 — Modèle de domaine (Client, Contract, Claim, User et énumérations).
3. Figure 3 — Séquence d'authentification et d'accès protégé par JWT.
4. Figure 4 — Composition Docker Compose (services `db` et `api`).
5. Figure 5 — Chaîne d'appel du frontend Angular (composant → intercepteur → API).

**Tableaux**
1. Tableau 1 — Besoins fonctionnels retenus.
2. Tableau 2 — Stack technique et justification.
3. Tableau 3 — Principaux points d'accès de l'API.
4. Tableau 4 — Règles métier implémentées.
5. Tableau 5 — Difficultés rencontrées et solutions.
6. Tableau 6 — Compétences acquises et modules ISIMG associés.
7. Tableau 7 — Limites identifiées et pistes d'amélioration (annexe/critique).

---

## 7. Points de vigilance pour la rédaction

- Ne pas écrire « autorisation par rôle opérationnelle » : les `@PreAuthorize` ne sont pas activés.
- Ne pas annoncer de couverture de tests chiffrée.
- Ne pas présenter Docker comme un déploiement de production.
- Présenter le frontend comme une initiative personnelle de montée en compétences.
- Présenter `admin/admin123` et `agent/agent123` comme des **données de démonstration de développement**, jamais comme des identifiants réels.
- Rester prudent sur l'historique Git (clone superficiel).
