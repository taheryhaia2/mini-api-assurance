# Dossier technique complet — Mini-API Assurance

> **Objet de ce document** : description exhaustive et vérifiée du dépôt `taheryhaia2/mini-api-assurance`, destinée à être fournie telle quelle à un autre assistant/IA chargé de rédiger un rapport (rapport de stage, mémoire, dossier technique).
>
> **Règle de lecture** : chaque affirmation technique provient d'une lecture directe du code ou d'une commande exécutée dans le dépôt. La section 21 distingue ce qui a été **exécuté et vérifié** de ce qui n'a **pas pu l'être** dans l'environnement de travail. La section 19 liste les **divergences entre la documentation existante et le code réel** : c'est la partie la plus importante à lire avant de rédiger, car le brouillon de rapport présent dans `docs/` est partiellement périmé.

---

## 1. Identité et contexte

| Élément | Valeur | Source |
|---|---|---|
| Nom du projet | Mini-API Assurance | `README.md`, `pom.xml` |
| Dépôt | `taheryhaia2/mini-api-assurance` | `README.md` |
| Coordonnées Maven | `com.assurance:mini-api-assurance:0.0.1-SNAPSHOT` | `pom.xml` |
| Nature | API REST de gestion d'assurance (clients, contrats, sinistres) + client web Angular | code |
| Cadre | Stage d'initiation, 1er–31 juillet 2026, chez **Vermeg**, département *Insurance Market Operations* | `docs/# Rapport de stage — Mini-API Assur.md` |
| Auteur | Taher Yahia — Licence Fondamentale en Informatique, Génie Logiciel (FIGL), 1re année | idem |
| Établissement | Institut Supérieur d'Informatique et de Multimédia de Gabès (ISIMG), Université de Gabès | idem |
| Encadrante entreprise | Mme Faten Kardous, *Lead Developer*, Insurance Market Operations | idem |
| Branche de travail | `arena/01a0ff05-mini-api-assurance` (issue de `master`) | `git branch` |
| Historique Git | **1 seul commit** : `1a354ad` (2026-07-30) « feat(frontend): page gestion employés (ADMIN) + roleGuard + UI conditionnelle » | `git log --all` |

**Point d'attention pour la rédaction** : le rapport décrit une méthodologie Git en branches avec historique *Conventional Commits*. Le dépôt tel qu'il existe ne contient qu'un unique commit ; l'historique de développement n'est donc pas observable dans ce clone. Ne pas citer un historique détaillé comme preuve.

---

## 2. Vue d'ensemble et périmètre

Le projet est composé de **deux applications distinctes** dans un même dépôt :

1. **Backend** (périmètre demandé par l'entreprise) : API REST Spring Boot 3, persistance PostgreSQL/JPA, sécurité JWT, documentation OpenAPI, conteneurisation Docker.
2. **Frontend** (présenté dans le rapport comme initiative personnelle hors périmètre) : application Angular consommant l'API.

Domaine métier couvert : un **client** souscrit plusieurs **contrats** ; chaque **contrat** porte plusieurs **sinistres**. Les utilisateurs (`User`, table `app_user`) sont hors du domaine métier : ils servent uniquement à l'authentification et à l'autorisation.

Explicitement **hors périmètre** (déclaré dans le rapport, confirmé par l'absence de code) : workflow d'instruction des sinistres (transitions de statut, calcul de remboursement), gestion des primes et échéanciers, édition de documents contractuels, intégration à un SI existant, déploiement en production, pagination, intégration continue.

---

## 3. Stack technique (versions lues dans les fichiers de build)

### Backend — `pom.xml`

| Composant | Version | Portée |
|---|---|---|
| Java | 17 (`<java.version>`) | — |
| Spring Boot (parent) | **3.5.16** | compile |
| `spring-boot-starter-web` | héritée | compile |
| `spring-boot-starter-data-jpa` | héritée | compile |
| `spring-boot-starter-validation` | héritée | compile |
| `spring-boot-starter-security` | héritée | compile |
| `spring-boot-starter-test` | héritée | test |
| `postgresql` (pilote JDBC) | héritée | runtime |
| `springdoc-openapi-starter-webmvc-ui` | **2.7.0** | compile |
| `jjwt-api` / `jjwt-impl` / `jjwt-jackson` | **0.12.6** | api / runtime / runtime |
| Maven wrapper | wrapper 3.3.4, distribution **3.9.16**, `only-script` | `.mvn/wrapper/maven-wrapper.properties` |

Plugin de build : `spring-boot-maven-plugin` (aucune configuration particulière). Le `pom.xml` contient des balises vides (`<name/>`, `<description/>`, `<licenses><license/></licenses>`, `<developers>`, `<scm>`) laissées par Spring Initializr, et un commentaire dupliqué quatre fois au-dessus de la dépendance springdoc.

### Frontend — `frontend/package.json`

| Composant | Version |
|---|---|
| Angular (`core`, `common`, `forms`, `router`, `platform-browser`, `compiler`) | `^21.2.0` |
| `@angular/cli`, `@angular/build` | `^21.2.19` |
| `chart.js` | `^4.5.1` |
| `rxjs` | `~7.8.0` |
| TypeScript | `~5.9.2` |
| `vitest` | `^4.0.8` (dev) |
| `jsdom` | `^28.0.0` (dev) |
| `prettier` | `^3.8.1` (dev) |
| Gestionnaire de paquets déclaré | `npm@10.9.2` |

Builders Angular : `@angular/build:application` (build), `@angular/build:dev-server` (serve), `@angular/build:unit-test` (test). Budgets de production : 500 kB en avertissement / 1 MB en erreur pour le bundle initial ; 4 kB / 8 kB par style de composant.

### Infrastructure

| Composant | Version / détail |
|---|---|
| PostgreSQL | image `postgres:16` (`docker-compose.yml`) |
| Image de build | `maven:3.9.9-eclipse-temurin-17` |
| Image d'exécution | `eclipse-temurin:17-jre` |

---

## 4. Arborescence et métriques du dépôt

```
mini-api-assurance/
├── pom.xml                      # build Maven backend
├── mvnw / mvnw.cmd / .mvn/      # wrapper Maven
├── Dockerfile                   # build multi-étapes
├── docker-compose.yml           # db + api
├── README.md                    # mode d'emploi (partiellement périmé, cf. §19)
├── docs/
│   ├── architecture.md          # diagramme de classes Mermaid (périmé, cf. §19)
│   └── # Rapport de stage — Mini-API Assur.md   # brouillon de rapport, 768 lignes
├── uploads/                     # documents de sinistres téléversés (2 .jpg commités)
├── src/main/java/com/assurance/mini_api_assurance/
│   ├── MiniApiAssuranceApplication.java
│   ├── config/       DataInitializer, OpenApiConfig, SecurityConfig
│   ├── controller/   Auth, Claim, Client, Contract, User
│   ├── domain/       Claim, Client, Contract, User + 4 enums
│   ├── dto/          12 records
│   ├── exception/    BusinessRuleException, NotFoundException, GlobalExceptionHandler
│   ├── mapper/       ClaimMapper, ClientMapper, ContractMapper
│   ├── repository/   Claim, Client, Contract, User
│   ├── security/     CustomUserDetails, CustomUserDetailsService, JwtAuthFilter, JwtService
│   └── service/      Claim, Client, Contract, User
├── src/main/resources/application.yaml
├── src/test/java/...  MiniApiAssuranceApplicationTests, service/ClaimServiceTest
└── frontend/          # application Angular
    ├── angular.json, package.json, package-lock.json, tsconfig*.json
    ├── public/favicon.ico
    └── src/
        ├── index.html, main.ts, styles.css
        └── app/
            ├── app.ts / app.html / app.css / app.config.ts / app.routes.ts / app.spec.ts
            ├── core/
            │   ├── guards/       auth.guard.ts, role.guard.ts
            │   ├── interceptors/ jwt.interceptor.ts
            │   ├── models/       auth.model.ts, client.model.ts, user.model.ts
            │   ├── navbar/       navbar.component.{ts,html,css}
            │   └── services/     auth, claim, client, contract, user (+ contract.spec.ts)
            └── features/
                ├── claim/     claim-list, claim-form, claim-detail
                ├── client/    client-list, client-form, client-detail
                ├── contract/  contract-list, contract-detail
                ├── dashboard/ dashboard.component
                ├── login/     login.component
                └── users/     user-list
```

**Métriques comptées** (`find` + `wc -l`, fichiers suivis par Git = 139) :

| Mesure | Valeur |
|---|---|
| Fichiers Java backend (`src/`) | 47 fichiers, **1 811 lignes** (tests inclus) |
| Fichiers TypeScript frontend (`frontend/src/`) | **1 071 lignes** |
| Fichiers de template HTML frontend | 14 |
| Fichiers de test backend | 2 |
| Fichiers `*.spec.ts` frontend | 9 |
| Composants Angular (`grep -rl "@Component"`) | 13 : 1 racine (`app.ts`) + 1 navbar + 11 de fonctionnalité |
| Routes Angular déclarées | 11 + 2 redirections |

---

## 5. Modèle de données

Quatre entités JPA, quatre énumérations. Toutes les énumérations sont persistées en `@Enumerated(EnumType.STRING)` (chaînes lisibles, pas d'ordinaux). Identifiants techniques : `GenerationType.IDENTITY`.

### `Client` → table `client`

| Champ | Type | Contraintes |
|---|---|---|
| `id` | `Long` | PK, identité |
| `lastName` | `String` | `nullable = false` |
| `firstName` | `String` | `nullable = false` |
| `email` | `String` | `nullable = false` (non unique) |
| `cin` | `String` | `unique = true, nullable = false` |
| `phoneNumber` | `String` | `nullable = false` |
| `address` | `String` | `nullable = false` |
| `active` | `boolean` | `nullable = false`, défaut `true` — support du *soft delete* |
| `birthDate` | `LocalDate` | nullable |
| `createdAt` | `LocalDate` | nullable en base, toujours fixé par le service |

### `Contract` → table `contract`

| Champ | Type | Contraintes |
|---|---|---|
| `id` | `Long` | PK, identité |
| `policyNumber` | `String` | `unique = true, nullable = false` (format `CT-<année>-<n>`) |
| `type` | `ContractType` | enum STRING, `nullable = false` |
| `client` | `Client` | `@ManyToOne(LAZY)`, `@JoinColumn(name="client_id", nullable=false)` |
| `startDate` / `endDate` | `LocalDate` | `nullable = false` |
| `status` | `ContractStatus` | enum STRING, `nullable = false` |
| `coverageAmount` | `BigDecimal` | `nullable = false` |
| `premiumAmount` | `BigDecimal` | `nullable = false` |

### `Claim` → table `claim`

| Champ | Type | Contraintes |
|---|---|---|
| `id` | `Long` | PK, identité |
| `claimNumber` | `String` | `unique = true, nullable = false` (format `CL-<année>-<n>`) |
| `contract` | `Contract` | `@ManyToOne(LAZY)`, `@JoinColumn(name="contract_id", nullable=false)` |
| `description` | `String` | nullable |
| `claimDate` | `LocalDate` | date de survenance |
| `declarationDate` | `LocalDate` | fixée par le service à `LocalDate.now()` |
| `estimatedAmount` | `BigDecimal` | `nullable = false` |
| `reimbursedAmount` | `BigDecimal` | nullable ; initialisé à `ZERO` à la création |
| `status` | `ClaimStatus` | enum STRING, `nullable = false` |
| `documentPath` | `String` | chemin du justificatif téléversé (non exposé par l'API) |

### `User` → table **`app_user`** (mappage explicite, `user` étant réservé en SQL)

| Champ | Type | Contraintes |
|---|---|---|
| `id` | `Long` | PK, identité |
| `firstName` / `lastName` | `String` | `nullable = false` |
| `email` | `String` | `unique = true, nullable = false` |
| `username` | `String` | `unique = true, nullable = false` |
| `password` | `String` | `nullable = false`, haché BCrypt |
| `role` | `Role` | enum STRING, `nullable = false` |

### Énumérations

| Enum | Valeurs |
|---|---|
| `ContractType` | `AUTO`, `HOME`, `HEALTH`, `LIFE` |
| `ContractStatus` | `ACTIVE`, `EXPIRED`, `TERMINATED` |
| `ClaimStatus` | `SUBMITTED`, `PROCESSING`, `ACCEPTED`, `REJECTED` |
| `Role` | `ADMIN`, `AGENT` |

**Remarque** : `PROCESSING`, `ACCEPTED` et `REJECTED` sont déclarés mais aucun code ne les produit — seul `SUBMITTED` est écrit, à la création. Aucun endpoint ne fait évoluer un sinistre. Idem pour `EXPIRED` : aucun traitement planifié ne bascule un contrat de `ACTIVE` à `EXPIRED` ; la valeur ne peut être atteinte que via `PUT /api/contracts/{id}`.

### Relations

`Client 1—* Contract` (FK `client_id`) et `Contract 1—* Claim` (FK `contract_id`), toutes deux `LAZY`. Aucun cascade ni `orphanRemoval` n'est déclaré. Aucun index explicite au-delà des contraintes d'unicité.

### Repositories Spring Data (8 méthodes dérivées déclarées)

| Repository | Méthodes déclarées | Utilisation réelle |
|---|---|---|
| `UserRepository` | `findByUsername`, `findByEmail` | utilisées |
| `ClientRepository` | `existsByCin`, `findByActiveTrue` | utilisées |
| `ContractRepository` | `findByClientId`, `existsByClientId` | `findByClientId` utilisée ; **`existsByClientId` déclarée mais jamais appelée** |
| `ClaimRepository` | `findByContractId`, `existsByContractId` | `findByContractId` utilisée ; **`existsByContractId` déclarée mais jamais appelée** |

---

## 6. Architecture en couches

Paquetage racine `com.assurance.mini_api_assurance`, organisé par responsabilité :

| Couche | Paquetage | Rôle |
|---|---|---|
| Domaine | `domain` | Entités JPA et énumérations |
| Transfert | `dto` | 12 `record` Java : contrats d'entrée et de sortie |
| Conversion | `mapper` | Classes utilitaires à méthodes `static` : `toEntity`, `toDto`, `updateEntity` |
| Accès données | `repository` | Interfaces `JpaRepository` |
| Métier | `service` | Règles métier, transactions `@Transactional` |
| HTTP | `controller` | `@RestController`, validation `@Valid`, codes de statut |
| Sécurité | `security` | Génération/vérification JWT, filtre, `UserDetailsService` |
| Configuration | `config` | `SecurityConfig`, `OpenApiConfig`, `DataInitializer` |
| Erreurs | `exception` | Exceptions métier + `@RestControllerAdvice` |

Caractéristiques vérifiées :

- **Injection de dépendances par constructeur** dans toutes les classes, sans `@Autowired` sur les champs ; les champs de dépendance sont `final`.
- Les **DTO sont des `record`** Java, donc immuables.
- Les **mappers sont des classes à méthodes statiques** (pas de MapStruct, pas de beans Spring).
- **Les entités ne sortent jamais des services** : les contrôleurs ne manipulent que des DTO. Effet concret : le mot de passe haché n'est exposé par aucune réponse (`UserResponseDto` ne contient pas le champ `password`).
- `ClaimMapper.toDto` n'expose pas `documentPath` : le chemin du fichier stocké reste interne.
- **DTO d'entrée différenciés** : `ClientCreateDto` contient `cin`, `ClientUpdateDto` ne le contient pas → il est techniquement impossible de modifier un CIN après création.
- `ClientService` **injecte `ContractRepository` sans jamais l'utiliser** (cf. §19, règle RM9).

---

## 7. API REST — inventaire exhaustif

**17 points d'accès** sont réellement exposés (comptés à partir des annotations `@*Mapping` des 5 contrôleurs).

| # | Méthode | Chemin | Entrée | Sortie | Code | Accès **effectif** |
|---|---|---|---|---|---|---|
| 1 | POST | `/api/auth/register` | `RegisterRequest` | texte `"User created: <username>"` | 201 | public |
| 2 | POST | `/api/auth/login` | `LoginRequest` | `AuthResponse` | 200 | public |
| 3 | POST | `/api/clients` | `ClientCreateDto` | `ClientResponseDto` | 201 | ADMIN ou AGENT |
| 4 | GET | `/api/clients` | — | `List<ClientResponseDto>` (clients actifs uniquement) | 200 | ADMIN ou AGENT |
| 5 | GET | `/api/clients/{id}` | — | `ClientResponseDto` | 200 | ADMIN ou AGENT |
| 6 | PUT | `/api/clients/{id}` | `ClientUpdateDto` | `ClientResponseDto` | 200 | ADMIN ou AGENT |
| 7 | DELETE | `/api/clients/{id}` | — | corps vide | 204 | ADMIN ou AGENT (*soft delete*) |
| 8 | POST | `/api/contracts` | `ContractCreateDto` | `ContractResponseDto` | 201 | ADMIN ou AGENT |
| 9 | GET | `/api/contracts` | — | `List<ContractResponseDto>` | 200 | ADMIN ou AGENT |
| 10 | GET | `/api/contracts/{id}` | — | `ContractResponseDto` | 200 | ADMIN ou AGENT |
| 11 | PUT | `/api/contracts/{id}` | `ContractUpdateDto` | `ContractResponseDto` | 200 | ADMIN ou AGENT |
| 12 | GET | `/api/contracts/client/{clientId}` | — | `List<ContractResponseDto>` | 200 | ADMIN ou AGENT |
| 13 | POST | `/api/contracts/{contractId}/claims` | `multipart/form-data` : champs de `ClaimCreateDto` + `file` optionnel | `ClaimResponseDto` | 201 | ADMIN ou AGENT |
| 14 | GET | `/api/contracts/{contractId}/claims` | — | `List<ClaimResponseDto>` | 200 | ADMIN ou AGENT |
| 15 | GET | `/api/claims` | — | `List<ClaimResponseDto>` | 200 | ADMIN ou AGENT |
| 16 | GET | `/api/claims/{id}` | — | `ClaimResponseDto` | 200 | ADMIN ou AGENT |
| 17 | GET | `/api/users` | — | `List<UserResponseDto>` | 200 | **ADMIN uniquement** |

Notes :

- Les sinistres sont exposés à la fois comme **sous-ressource du contrat** (13, 14) et comme **ressource racine** (15, 16).
- Le point d'accès 13 est le seul à consommer du `multipart/form-data` (`consumes = MediaType.MULTIPART_FORM_DATA_VALUE`) ; le DTO y est lié par `@ModelAttribute` et non `@RequestBody`. **Conséquence : `@Valid` n'y est pas appliqué** — les annotations Bean Validation de `ClaimCreateDto` ne sont pas évaluées sur cette route.
- Aucun endpoint de suppression de contrat, ni de suppression/modification de sinistre.
- Aucune pagination, aucun tri, aucun filtrage sur les listes.

### Contrats de données (les 12 `record`)

**Entrée**

```java
record RegisterRequest(@NotBlank String username, @NotBlank String password,
                       @NotNull Role role, @NotBlank String firstName,
                       @NotBlank String lastName, @NotBlank String email)
record LoginRequest(@NotBlank String username, @NotBlank String password)

record ClientCreateDto(@NotBlank String lastName, @NotBlank String firstName,
                       @Email @NotBlank String email,
                       @NotBlank @Pattern(regexp="\\d{8}", message="Le CIN doit contenir exactement 8 chiffres") String cin,
                       @NotBlank @Pattern(regexp="\\d{8}", message="Le téléphone doit contenir exactement 8 chiffres") @NotBlank String phoneNumber,
                       @NotBlank String address, @NotNull LocalDate birthDate)
record ClientUpdateDto(@NotBlank String lastName, @NotBlank String firstName,
                       @Email @NotBlank String email, @NotBlank String phoneNumber,
                       @NotBlank String address, @NotNull LocalDate birthDate)   // pas de cin
record ContractCreateDto(@NotNull Long clientId, @NotNull ContractType type,
                         @NotNull LocalDate startDate, @NotNull LocalDate endDate,
                         @NotNull @Positive BigDecimal coverageAmount,
                         @NotNull @Positive BigDecimal premiumAmount)
record ContractUpdateDto(@Positive BigDecimal coverageAmount, @Positive BigDecimal premiumAmount,
                         LocalDate endDate, String status)      // status = String, pas l'enum
record ClaimCreateDto(@NotBlank String description, @NotNull LocalDate claimDate,
                      @NotNull @Positive BigDecimal estimatedAmount)
```

**Sortie**

```java
record AuthResponse(String token, String username, Role role)
record UserResponseDto(Long id, String username, String firstName, String lastName, String email, Role role)
record ClientResponseDto(Long id, String lastName, String firstName, String email, String cin,
                         String phoneNumber, String address, LocalDate birthDate, LocalDate createdAt)
record ContractResponseDto(Long id, String policyNumber, Long clientId, ContractType type,
                           LocalDate startDate, LocalDate endDate, ContractStatus status,
                           BigDecimal coverageAmount, BigDecimal premiumAmount)
record ClaimResponseDto(Long id, String claimNumber, Long contractId, String description,
                        LocalDate claimDate, LocalDate declarationDate, BigDecimal estimatedAmount,
                        BigDecimal reimbursedAmount, ClaimStatus status)
```

Points notables : `ClientCreateDto` répète `@NotBlank` sur `phoneNumber` (annotation dupliquée, sans effet) ; `ContractUpdateDto.status` est une `String` convertie manuellement dans le service, pas l'enum ; les réponses ne contiennent que les identifiants (`clientId`, `contractId`), jamais les objets imbriqués.

---

## 8. Sécurité

### Authentification JWT

- `JwtService` (JJWT 0.12.6) : `Jwts.builder().subject(username).issuedAt(now).expiration(now + 24 h).signWith(key)` — **validité 24 heures**, `1000 * 60 * 60 * 24` ms codé en dur.
- Clé HMAC obtenue par `Decoders.BASE64.decode(secretKey)` puis `Keys.hmacShaKeyFor(...)` ; le secret est lu par `@Value("${jwt.secret}")`.
- Le secret est **écrit en clair dans `src/main/resources/application.yaml`** : `jwt.secret: "U3VwZXJTZWNyZXRLZXlQb3VyTGVTdGFnZURhc3N1cmFuY2UyMDI0IQ=="` (chaîne Base64, décodée : `SuperSecretKeyPourLeStageDassurance2024!`).
- Le jeton ne porte **aucune revendication de rôle** : seul le `subject` (nom d'utilisateur) y figure. Le rôle est relu en base à chaque requête via `CustomUserDetailsService`.
- `isTokenValid` vérifie l'égalité du `subject` et l'expiration.
- `JwtAuthFilter extends OncePerRequestFilter`, inséré avant `UsernamePasswordAuthenticationFilter` : lit l'en-tête `Authorization`, exige le préfixe `Bearer `, extrait le sujet, charge l'utilisateur, alimente le `SecurityContextHolder`. **Le filtre ne rejette jamais lui-même une requête** et **n'intercepte aucune exception** : un jeton expiré ou malformé fait remonter une exception JJWT au lieu de produire un 401 propre.

### Configuration de la chaîne (`SecurityConfig`)

```java
.cors(Customizer.withDefaults())
.csrf(csrf -> csrf.disable())
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
    .requestMatchers("/api/users/**").hasRole("ADMIN")
    .requestMatchers("/api/clients/**", "/api/contracts/**", "/api/claims/**").hasAnyRole("ADMIN", "AGENT")
    .anyRequest().authenticated())
.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.authenticationProvider(authenticationProvider())
.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
```

- Mots de passe hachés **BCrypt** (`BCryptPasswordEncoder`).
- `DaoAuthenticationProvider` câblé sur `CustomUserDetailsService` (charge `User` par `username`, lève `UsernameNotFoundException` sinon).
- `CustomUserDetails` convertit le rôle en autorité `ROLE_<ADMIN|AGENT>` ; les quatre méthodes de cycle de vie (`isAccountNonExpired`, `isAccountNonLocked`, `isCredentialsNonExpired`, `isEnabled`) renvoient `true` en dur, chacune marquée d'un `// TODO: For V2`.
- Session **STATELESS**, **CSRF désactivé** (justifié : pas de cookies, jeton transmis en en-tête).

### Autorisation : état réel, à décrire avec précision

Deux mécanismes coexistent et **leur statut diffère** :

1. **Autorisation par URL — ACTIVE.** C'est elle qui protège réellement l'API. `/api/users/**` exige le rôle `ADMIN` ; `/api/clients/**`, `/api/contracts/**` et `/api/claims/**` exigent `ADMIN` ou `AGENT` ; toute autre requête exige une authentification.
2. **Autorisation par méthode — INOPÉRANTE.** Cinq annotations `@PreAuthorize` sont présentes dans le code (`ClaimController` ×3 `hasAnyRole('ADMIN','AGENT')`, `ClientController.deleteClient` `hasRole('ADMIN')`, `ContractController.updateContract` `hasRole('ADMIN')`), plus une sixième **commentée** sur `ClientController.updateClient`. Or **`@EnableMethodSecurity` n'apparaît nulle part dans le projet** (vérifié par `grep -rn "EnableMethodSecurity" src/` → aucune occurrence). Ces annotations sont donc du code mort.

**Conséquence vérifiable à décrire** : la seule restriction par rôle effective est l'accès à `/api/users/**`. Un compte `AGENT` peut supprimer un client (route 7) et modifier un contrat (route 11), alors que les annotations laissent croire le contraire. C'est l'écart sécurité le plus important du projet.

### CORS

`CorsConfigurationSource` déclaré et activé : origine unique autorisée `http://localhost:4200` (le serveur de développement Angular), méthodes `GET, POST, PUT, DELETE, OPTIONS`, en-têtes `*`, `allowCredentials = true`, appliqué à `/**`.

---

## 9. Règles métier réellement implémentées

Inventaire établi ligne à ligne dans les services. Les messages d'erreur sont cités littéralement.

| Réf. | Règle | Emplacement | Message / comportement |
|---|---|---|---|
| **RM1** | Le CIN doit être unique | `ClientService.createClient` | `BusinessRuleException("Un client avec ce CIN existe déjà.")` |
| **RM2** | Le client doit avoir au moins 18 ans (`birthDate` null **ou** postérieur à aujourd'hui − 18 ans) | `ClientService.createClient` | `BusinessRuleException("Le client doit avoir au moins 18 ans.")` |
| **RM3** | `createdAt` et `active` d'un client sont fixés par le serveur | `ClientService.createClient` | valeurs imposées (`LocalDate.now()`, `true`) |
| **RM4** | La liste des clients ne contient que les clients actifs | `ClientService.listClients` | `findByActiveTrue()` |
| **RM5** | Un client archivé ne peut plus être modifié | `ClientService.updateClient` | `BusinessRuleException("Impossible de modifier un client archivé.")` |
| **RM6** | Le CIN n'est pas modifiable après création | `ClientUpdateDto` + `ClientMapper.updateEntity` | absence structurelle du champ |
| **RM7** | La suppression d'un client est logique (archivage), jamais physique | `ClientService.deleteClient` | `active = false`, aucune suppression SQL |
| **RM8** | Un contrat ne peut être créé que pour un client existant | `ContractService.createContract` | `NotFoundException("Client not found with ID: …")` |
| **RM9** | La date de fin ne peut précéder la date de début | `ContractService.createContract` | `BusinessRuleException("End date must be after start date")` |
| **RM10** | Un contrat créé est nécessairement `ACTIVE` | `ContractService.createContract` | valeur imposée |
| **RM11** | Le numéro de police est généré par le serveur | `ContractService.generatePolicyNumber` | `"CT-" + année + "-" + (millis % 100000)` |
| **RM12** | Un statut de contrat invalide est rejeté à la mise à jour | `ContractService.updateContract` | `BusinessRuleException("Invalid status value: …")` |
| **RM13** | Aucun sinistre sur un contrat non `ACTIVE` | `ClaimService.createClaim` | `BusinessRuleException("Cannot file a claim: Contract is not ACTIVE")` |
| **RM14** | La date de survenance doit tomber dans la période de couverture | `ClaimService.createClaim` | `BusinessRuleException("Claim date must be within the contract coverage period")` |
| **RM15** | Un sinistre créé est `SUBMITTED`, remboursement initialisé à zéro, date de déclaration = aujourd'hui, numéro généré | `ClaimService.createClaim` | valeurs imposées ; `"CL-" + année + "-" + (millis % 100000)` |
| **RM16** | Le justificatif est renommé avec un UUID avant stockage | `ClaimService.createClaim` | `UUID.randomUUID() + extension d'origine`, chemin en base |
| **RM17** | Un sinistre ne peut être listé que pour un contrat existant | `ClaimService.getClaimsByContractId` | `NotFoundException("Contract not found with ID: …")` |
| **RM18** | Nom d'utilisateur et courriel uniques à l'inscription | `UserService.createUser` | `RuntimeException("Username already exists")` / `("Email already exists")` |
| **RM19** | Le mot de passe est haché avant persistance | `UserService.createUser` | `passwordEncoder.encode(...)` |

### Ce qui n'est PAS implémenté (important pour un rapport honnête)

- **Contrôle « un client rattaché à un contrat ne peut être supprimé » : absent.** `ClientService.deleteClient` archive le client sans vérifier l'existence de contrats. `ContractRepository` est injecté dans `ClientService` mais **jamais appelé** ; `existsByClientId` est déclarée dans le repository et **jamais utilisée**. Un client porteur de contrats peut donc être archivé, et ses contrats restent listés.
- **Aucune revalidation des dates à la mise à jour d'un contrat** : `updateContract` écrit `endDate` sans le comparer à `startDate`, et sans vérifier que `endDate` est non nul — un corps de requête sans `endDate` met la colonne à `null` et provoque une violation de contrainte `nullable = false` à la validation de transaction.
- **Aucune transition de statut de sinistre**, aucun calcul ou enregistrement de `reimbursedAmount` au-delà du zéro initial.
- **Aucun endpoint de restitution du justificatif** téléversé, et `documentPath` n'est exposé nulle part.
- **Aucun passage automatique à `EXPIRED`** (pas de tâche planifiée).
- **Génération d'identifiants métier non fiable** : `System.currentTimeMillis() % 100000` peut produire une collision ; les colonnes étant `unique`, la collision se traduit par une exception de contrainte remontée en HTTP 400.
- **Écriture du fichier avant l'enregistrement en base** : si la persistance échoue, le fichier reste orphelin dans `uploads/` (aucune compensation, aucune transaction sur le système de fichiers).

---

## 10. Validation des entrées

Bean Validation (Jakarta) via `spring-boot-starter-validation`, activée par `@Valid` sur les paramètres de contrôleur — **sauf sur la route de création de sinistre** (§7). Annotations employées : `@NotBlank`, `@NotNull`, `@Email`, `@Positive`, `@Pattern`. Deux règles de format spécifiques au contexte tunisien : **CIN et numéro de téléphone à exactement 8 chiffres** (`\d{8}`).

`server.error.include-message: always` est activé dans `application.yaml` afin que le message d'exception soit renvoyé au client.

---

## 11. Gestion des erreurs

`GlobalExceptionHandler` (`@RestControllerAdvice`) ne déclare que **deux** gestionnaires :

| Exception interceptée | Statut renvoyé | Corps |
|---|---|---|
| `RuntimeException` (toutes) | **400 Bad Request** | `{"error": "<message>"}` |
| `MethodArgumentNotValidException` | **400 Bad Request** | `{"error": "<champ> : <message>"}` (première erreur seulement) |

`BusinessRuleException` et `NotFoundException` étendent `RuntimeException` **sans gestionnaire dédié**. Conséquences réelles :

- Une ressource inexistante renvoie **400 au lieu de 404**.
- Un échec d'authentification (`BadCredentialsException`, `UsernameNotFoundException`) renvoie **400 au lieu de 401**.
- Une violation de règle métier renvoie 400 là où un 409/422 serait sémantiquement correct.
- En cas de conflit d'unicité non couvert par un contrôle applicatif, le message renvoyé est celui de l'exception JPA/SQL interne (fuite d'informations techniques).
- Le gestionnaire de validation ne renvoie que la **première** erreur de champ, pas la liste complète.
- Aucun format d'erreur normalisé (pas de RFC 9457 / Problem Details), pas de code d'erreur métier, pas d'horodatage ni d'identifiant de corrélation.

---

## 12. Téléversement de documents

Implémenté dans `ClaimService`, déclenché par `POST /api/contracts/{contractId}/claims` avec une partie `file` facultative :

- Dossier de destination : `Paths.get("uploads")`, **relatif au répertoire de travail courant**, créé au démarrage du constructeur du service (`Files.createDirectories`), avec `RuntimeException("Impossible de créer le dossier de stockage.")` en cas d'échec.
- Nom de fichier : `UUID.randomUUID()` + extension extraite du nom d'origine (recherche du dernier `.`).
- Copie par `Files.copy(file.getInputStream(), destinationFile)` ; chemin absolu persisté dans `Claim.documentPath`.
- **Aucune limite de taille** (`spring.servlet.multipart.max-file-size` non configuré → valeurs par défaut Spring Boot), **aucun contrôle de type MIME ni d'extension**, **aucune analyse antivirus**, **aucun point d'accès de téléchargement**.
- En conteneur, le dossier `uploads/` vit dans la couche éphémère de l'image : **les documents sont perdus à la recréation du conteneur** (aucun volume déclaré pour ce chemin dans `docker-compose.yml`).
- Le dépôt contient déjà **2 fichiers `uploads/*.jpg` commités dans Git** (15 829 octets chacun) : cf. §19 pour la cause.

---

## 13. Données de démonstration (`DataInitializer`)

`CommandLineRunner` exécuté **à chaque démarrage**, idempotent par tests d'existence :

| Donnée | Condition | Détail |
|---|---|---|
| Utilisateur `admin` / `admin123`, rôle `ADMIN` | si `findByUsername("admin")` est vide | Admin System, `admin@assurance.com` |
| Utilisateur `agent` / `agent123`, rôle `AGENT` | si `findByUsername("agent")` est vide | Agent Field, `agent@assurance.com` |
| Client « Ahmed Ben Ali » | si `clientRepository.count() == 0` | CIN `12345678`, né le 15/05/1990, Tunis |
| Contrat `CT-2026-00001` | idem | `AUTO`, `ACTIVE`, début −1 mois, fin +1 an, couverture 50 000, prime 1 200 |
| Contrat `CT-2026-00002` | idem | `HOME`, `TERMINATED`, −2 ans → −1 an, couverture 30 000, prime 800 |

Le contrat `TERMINATED` sert à démontrer immédiatement la règle RM13. Chaque création est tracée par un `System.out.println(">> [DataInit] …")`. **Aucun profil Spring ne conditionne ce composant** : il s'exécute aussi en production si l'image y est déployée. Les identifiants de démonstration sont en dur dans le code source.

---

## 14. Configuration et exécution

### `src/main/resources/application.yaml`

```yaml
server:
  error:
    include-message: always
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/assurance_db}
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
    password: ${SPRING_DATASOURCE_PASSWORD:admin}
  jpa:
    hibernate:
      ddl-auto: ${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
    show-sql: true
    open-in-view: false
jwt:
  secret: "U3VwZXJTZWNyZXRLZXlQb3VyTGVTdGFnZURhc3N1cmFuY2UyMDI0IQ=="
```

Points à commenter dans un rapport :

- Les **paramètres de base de données sont externalisés** avec valeurs par défaut (principe *12-factor*), mais **le secret JWT ne l'est pas**.
- `ddl-auto: update` : schéma géré par Hibernate, non versionné, aucune migration, aucun retour arrière possible.
- `open-in-view: false` : choix délibéré qui rend visibles les accès hors transaction aux associations `LAZY` — c'est ce qui impose que la conversion en DTO se fasse **dans** la méthode `@Transactional`.
- `show-sql: true` : le SQL est journalisé sur la sortie standard, y compris en conteneur.
- Un seul fichier de configuration : **aucun profil** `dev` / `test` / `prod`, donc aucun moyen de désactiver `DataInitializer` ou le SQL affiché selon l'environnement.
- Aucune configuration de pool de connexions, de timeout, de limites multipart, de journalisation structurée ni d'actuator (`spring-boot-starter-actuator` absent du `pom.xml`).

### `Dockerfile` (build multi-étapes)

Étape 1 : `maven:3.9.9-eclipse-temurin-17`, copie de `pom.xml` puis de `src`, `mvn -q -DskipTests package`. Étape 2 : `eclipse-temurin:17-jre`, copie de `target/*.jar` en `app.jar`, `EXPOSE 8080`, `ENTRYPOINT ["java","-jar","app.jar"]`.

Limites vérifiables : **tests ignorés** à la construction (`-DskipTests`) ; `pom.xml` et `src` copiés ensemble, donc toute modification de source invalide le cache de dépendances ; exécution en tant qu'utilisateur **`root`** (aucun `USER`) ; aucune `HEALTHCHECK` ; aucune limite mémoire (`-Xmx`) ; aucun argument JVM de conteneur.

### `docker-compose.yml`

| Service | Image | Ports | Particularités |
|---|---|---|---|
| `db` (`assurance-db`) | `postgres:16` | `5432:5432` | `POSTGRES_DB=assurance_db`, `postgres`/`admin` en clair, volume nommé `pgdata`, `healthcheck` = `pg_isready -U postgres -d assurance_db` (5 s / 5 s / 10 essais) |
| `api` (`assurance-api`) | construite depuis le `Dockerfile` | `8080:8080` | `depends_on: db: condition: service_healthy`, URL JDBC vers `db:5432`, `SPRING_JPA_HIBERNATE_DDL_AUTO=update` |

Le démarrage ordonné est garanti par le couple *healthcheck* + `condition: service_healthy` (l'API n'est lancée qu'une fois PostgreSQL réellement prêt). Limites : identifiants en clair dans le fichier, pas de volume pour `uploads/`, pas de limites de ressources, pas de réseau personnalisé explicite, pas de politique de redémarrage, et le service `frontend` n'est pas conteneurisé.

### `.dockerignore`

Exclut `target/`, `.git/`, `.idea/`, `*.iml`, `docs/`, `README.md`. Le dossier `frontend/` **n'est pas exclu**, bien que le `Dockerfile` ne copie que `pom.xml` et `src` (sans effet réel, mais incohérent).

---

## 15. Documentation OpenAPI / Swagger

`springdoc-openapi-starter-webmvc-ui` 2.7.0 génère la spécification à partir des signatures de contrôleurs. `OpenApiConfig` déclare : titre « Mini-API Assurance », version `1.0`, description « REST API for managing clients, contracts and claims with JWT authentication », un `SecurityScheme` HTTP `bearer` (`bearerFormat: JWT`) nommé `bearerAuth`, et un `SecurityRequirement` global qui applique le schéma à toutes les opérations.

Accès : `http://localhost:8080/swagger-ui/index.html` et `http://localhost:8080/v3/api-docs`. Les deux chemins sont en `permitAll()`.

Limites : la documentation est **générique** — aucune annotation `@Operation`, `@ApiResponse`, `@Tag` ni `@Schema` dans le code, donc pas de description des règles métier, des codes d'erreur réels (tous 400) ni des exemples ; le schéma de sécurité global est déclaré sur toutes les routes alors que `/api/auth/**` est public ; la route multipart n'est pas décrite plus précisément que sa signature.

---

## 16. Tests backend

**Deux fichiers de test**, dans `src/test/java/com/assurance/mini_api_assurance/` :

1. `MiniApiAssuranceApplicationTests` (13 lignes) — `@SpringBootTest` + `contextLoads()` vide. Vérifie le démarrage du contexte Spring ; **nécessite une base PostgreSQL accessible** pour passer.
2. `service/ClaimServiceTest` (56 lignes) — JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`, `@Mock` sur les deux repositories, `@InjectMocks` sur le service). Un seul test : `createClaim_shouldThrowBusinessRuleException_whenContractIsTerminated` — construit un contrat `TERMINATED`, simule `findById`, et vérifie que `createClaim(contractId, dto, null)` lève `BusinessRuleException`. **Valide la règle RM13**, rendu possible par l'injection par constructeur.

**Couverture réelle** : 1 test unitaire pour 19 règles métier recensées en §9 ; aucun test de contrôleur, aucun test d'intégration HTTP (`MockMvc`), aucun test de la sécurité/JWT, aucun test des autres services, aucun test du frontend Angular côté backend, **aucun outil de mesure de couverture** (ni JaCoCo ni autre plugin dans le `pom.xml`), **aucune CI** (aucun fichier `.github/workflows`, `.gitlab-ci.yml`, `Jenkinsfile` dans le dépôt).

---

## 17. Frontend Angular

### Structure et conventions

Application **standalone** (aucun `NgModule`), Angular 21, avec `provideRouter` et `provideHttpClient(withInterceptors([jwtInterceptor]))` dans `app.config.ts`. État local géré par **signaux** (`signal()`), pas de RxJS `BehaviorSubject`. Contrôle de flux moderne dans les templates (`@if`). Trois routes chargées paresseusement (`loadComponent`) : `client-detail`, `contract-detail`, `claim-detail`.

### Routes (`app.routes.ts`)

| Chemin | Composant | Garde | Chargement |
|---|---|---|---|
| `login` | `LoginComponent` | — | eager |
| `dashboard` | `DashboardComponent` | `authGuard` | eager |
| `clients` | `ClientListComponent` | `authGuard` | eager |
| `clients/new` | `ClientFormComponent` | `authGuard` | eager |
| `clients/:id` | `ClientDetailComponent` | `authGuard` | lazy |
| `contracts` | `ContractListComponent` | `authGuard` | eager |
| `contracts/:id` | `ContractDetailComponent` | `authGuard` | lazy |
| `claims` | `ClaimListComponent` | `authGuard` | eager |
| `claims/new` | `ClaimFormComponent` | `authGuard` | eager |
| `claims/:id` | `ClaimDetailComponent` | `authGuard` | lazy |
| `users` | `UserListComponent` | `authGuard` + `roleGuard` | eager |
| `''` | → redirection `login` | — | — |
| `**` | → redirection `login` | — | — |

### Mécanisme d'authentification côté client

- `AuthService` : appelle `POST http://localhost:8080/api/auth/login`, stocke `token`, `username` et `role` dans le **`localStorage`** (clés `jwt_token`, `username`, `role`), expose un signal réactif `roleSignal` initialisé depuis le `localStorage` au démarrage, et fournit `isLoggedIn()`, `getUsername()`, `getRole()`, `isAdmin()`, `logout()`.
- `jwtInterceptor` (`HttpInterceptorFn`) : ajoute `Authorization: Bearer <token>` à toute requête sortante si un jeton est présent. **Ne traite pas les réponses** : un 401 ne déclenche ni déconnexion ni redirection.
- `authGuard` : autorise si `isLoggedIn()` (simple présence du jeton en `localStorage` — **aucune vérification de validité ni d'expiration**), sinon redirige vers `/login`.
- `roleGuard` : autorise si `isAdmin()`, sinon redirige vers `/dashboard`.
- `app.html` : affiche la barre de navigation `@if (authService.isLoggedIn())`, puis `<router-outlet />`.
- `navbar.component.html` : liens Accueil / Clients / Contrats / Sinistres, plus **Employés uniquement si `authService.isAdmin()`**, nom d'utilisateur affiché et bouton « Déconnexion ».

**À souligner** : la restriction par rôle côté frontend (garde + lien conditionnel) **double** la restriction par URL côté backend sur `/api/users/**`. Ce couplage frontend/backend est le point le plus abouti du projet en matière d'autorisation — et il est postérieur au brouillon de rapport (cf. §19).

### Services HTTP

`ClientService`, `ContractService`, `ClaimService`, `UserService` encapsulent `HttpClient`. **L'URL de base `http://localhost:8080` est codée en dur dans les cinq services** (aucun `environment.ts`, aucune variable d'environnement de build). Méthodes disponibles : création et lecture des clients, lecture des contrats (liste, détail, par client), lecture des sinistres (liste globale, détail, par contrat), lecture des utilisateurs et inscription via `/api/auth/register`.

**Opérations backend non exposées dans l'interface** : `PUT /api/clients/{id}`, `DELETE /api/clients/{id}`, `POST /api/contracts`, `PUT /api/contracts/{id}`. Autrement dit, la modification et la suppression d'un client, ainsi que toute création ou modification de contrat, sont impossibles depuis l'interface graphique — elles restent accessibles via Swagger.

### Écrans

| Écran | Contenu fonctionnel vérifié |
|---|---|
| `login` | formulaire `username`/`password`, `FormsModule`, message d'erreur « Identifiants invalides » sur échec, redirection `/dashboard` au succès |
| `dashboard` | 3 compteurs en signaux (clients, contrats, sinistres) alimentés par 3 appels parallèles ; **graphique en barres `chart.js`** dessiné seulement quand les 3 réponses sont revenues (`apiCallsReturned === 3`), avec destruction du graphique précédent (`Chart.getChart(...)?.destroy()`) ; couleurs `#0052cc`, `#00875a`, `#de350b` |
| `client-list` | tableau des clients chargé au `ngOnInit`, liens de navigation |
| `client-form` | création d'un client ; affiche le champ `error` de la réponse d'erreur de l'API |
| `client-detail` | fiche client + ses contrats via `GET /api/contracts/client/{id}` |
| `contract-list` | liste des contrats, navigation vers le détail |
| `contract-detail` | contrat + ses sinistres via `GET /api/contracts/{id}/claims` |
| `claim-list` | liste **globale** des sinistres (`GET /api/claims`) |
| `claim-form` | sélection d'un client puis **filtrage côté client** de ses contrats (à partir de `GET /api/contracts`), envoi en `FormData` avec partie `file` optionnelle vers `/api/contracts/{contractId}/claims`, affichage du message d'erreur métier renvoyé par l'API, réinitialisation du formulaire au succès |
| `claim-detail` | détail d'un sinistre |
| `user-list` | liste des employés + formulaire d'inscription d'un utilisateur (rôle par défaut `AGENT`), accessible uniquement à `ADMIN` |

### Style

`styles.css` (21 lignes) définit des variables CSS (`--primary: #0052cc`, `--bg: #f0f4f8`, `--danger: #c0392b`, …) et une police `'Segoe UI', system-ui`. Chaque composant possède son propre fichier CSS ; 14 templates HTML au total.

### Limites vérifiées du frontend

- URL d'API codée en dur dans 5 services → impossible à déployer sans recompilation.
- Jeton en `localStorage` → exposé aux attaques XSS ; pas de rafraîchissement ni de révocation.
- `authGuard` ne vérifie pas l'expiration du jeton ; aucun gestionnaire global de 401.
- Aucune validation de formulaire côté client (pas de `ReactiveFormsModule`, pas de validateurs) : les règles de format (CIN 8 chiffres, âge ≥ 18 ans) ne sont découvertes qu'après l'appel réseau.
- Le filtrage des contrats par client est fait **côté client** à partir de la liste complète, alors que `GET /api/contracts/client/{clientId}` existe.
- Aucune gestion d'état globale, aucun intercepteur d'erreur, aucun *retry*, aucun état de chargement (« spinner ») ni message d'erreur réseau générique.
- Aucun fichier `environment.ts`, aucun proxy de développement configuré dans `angular.json`.
- `package-lock.json` est versionné (reproductibilité des dépendances) ; `frontend/node_modules` et `frontend/dist` sont exclus par `frontend/.gitignore`.

---

## 18. État des tests frontend (exécutés — résultat négatif)

`npx ng test --watch=false` (builder `@angular/build:unit-test`, Vitest) **échoue à la compilation** : 9 fichiers `*.spec.ts` existent, **8 sont cassés**.

| Fichier | Erreur |
|---|---|
| `features/claim/claim-list/claim-list.spec.ts` | `TS2305: Module '"./claim-list"' has no exported member 'ClaimList'` |
| `features/claim/claim-form/claim-form.spec.ts` | `TS2305: … no exported member 'ClaimForm'` |
| `features/claim/claim-detail/claim-detail.spec.ts` | `TS2305: … no exported member 'ClaimDetail'` |
| `features/client/client-detail/client-detail.spec.ts` | `TS2305: … no exported member 'ClientDetail'` |
| `features/contract/contract-list/contract-list.spec.ts` | `TS2305: … no exported member 'ContractList'` |
| `features/contract/contract-detail/contract-detail.spec.ts` | `TS2305: … no exported member 'ContractDetail'` |
| `features/users/user-list/user-list.spec.ts` | `TS2305: … no exported member 'UserList'` |
| `core/services/contract.spec.ts` | `TS2307: Cannot find module './contract'` |

Cause : ces fichiers sont les squelettes générés par le CLI Angular, qui n'ont jamais été adaptés. Ils importent des noms de classe par défaut (`ClaimList`, `ContractDetail`, …) alors que les classes réelles sont suffixées `Component` (`ClaimListComponent`, `ContractDetailComponent`, …) ; `contract.spec.ts` teste un service `Contract` inexistant (le vrai fichier est `contract.service.ts`, classe `ContractService`).

Le neuvième fichier, `app.spec.ts`, compile mais contient une assertion vouée à l'échec : `expect(compiled.querySelector('h1')?.textContent).toContain('Hello, frontend')` alors que `app.html` ne contient **aucune balise `<h1>`** (seulement `@if (authService.isLoggedIn()) { <app-navbar /> }` et `<router-outlet />`).

**Conclusion à écrire sans détour** : le frontend n'a **aucun test fonctionnel** ; la suite de tests est non exécutable en l'état.

---

## 19. Divergences entre documentation existante et code réel ⚠️

C'est la section la plus importante avant rédaction. Le brouillon de rapport (`docs/# Rapport de stage — Mini-API Assur.md`, 768 lignes), le `README.md` et `docs/architecture.md` sont **partiellement périmés** : ils décrivent un état antérieur du projet, antérieur au dernier commit (`1a354ad`, page de gestion des employés + `roleGuard`).

### A. Affirmations du brouillon de rapport contredites par le code

| Réf. rapport | Affirmation du rapport | Réalité vérifiée dans le code |
|---|---|---|
| **§4.5, tableau 3, RM9** | « Un client rattaché à un contrat ne peut être supprimé — `ClientService` — Exception métier » | **Faux.** `deleteClient` archive sans aucun contrôle ; `contractRepository` est injecté mais jamais utilisé ; `existsByClientId` n'est jamais appelé. |
| **§2.2, tableau 1, BF4** | « Supprimer un client sans contrat rattaché — Réalisé » | **Faux**, pour la même raison : la condition « sans contrat rattaché » n'est pas implémentée. |
| **§5.2** | « la suppression d'un client porteur de contrats est refusée » | **Faux.** Rien ne la refuse. |
| **§4.5** | « **Treize** points d'accès sont exposés » | **17** points d'accès (comptés sur les annotations des 5 contrôleurs). |
| **§4.3** | « **Trois** méthodes seulement ont été déclarées : `findByUsername`, `findByContractId` et `existsByClientId` » | **8** méthodes dérivées déclarées ; `existsByClientId` est précisément celle qui n'est jamais utilisée. |
| **§4.8** | « Quatre écrans ont été réalisés : connexion, tableau de bord, liste des clients et formulaire de création » | **11 écrans** (login, dashboard, client-list, client-form, client-detail, contract-list, contract-detail, claim-list, claim-form, claim-detail, user-list) + navbar. |
| **§4.8** | « Les écrans relatifs aux **contrats et aux sinistres n'existent pas** » | **Faux.** `contract-list`, `contract-detail`, `claim-list`, `claim-form`, `claim-detail` existent et sont routés. |
| **§4.8** | « la modification et la suppression d'un client ne sont pas accessibles depuis l'interface » | **Vrai et toujours d'actualité** (le `ClientService` Angular ne contient que `createClient`, `getAllClients`, `getClientById`). |
| **§5.4 / §6.3 / conclusion** | « L'autorisation par rôle **n'est pas effective** », « tout utilisateur authentifié… », « aucune différenciation par rôle » | **Partiellement faux désormais.** L'autorisation **par URL** différencie bien les rôles : `/api/users/**` exige `ADMIN`. En revanche l'affirmation reste exacte pour les 5 `@PreAuthorize` inopérantes et pour le fait qu'un `AGENT` peut supprimer un client ou modifier un contrat. |
| **§4.5, extrait 3** | Signature `createClaim(Long contractId, ClaimCreateDto dto)` | La signature réelle est `createClaim(Long contractId, ClaimCreateDto dto, MultipartFile file)`. |
| **§4.2, extrait 1** | `ClientCreateDto` avec `@NotBlank String cin` et `@NotBlank String phoneNumber` | Les deux champs portent désormais `@Pattern(regexp = "\\d{8}")`. |
| **§1.4 / §6.2** | Méthodologie Git en branches, historique *Conventional Commits* exploitable | Le clone ne contient **qu'un commit**. Le message unique suit bien la convention (`feat(frontend): …`), mais aucun historique n'est consultable. |

### B. Affirmations exactes et directement réutilisables

Les points suivants du brouillon sont **conformes au code** : architecture en couches et paquetages (§3.3) ; injection par constructeur et DTO sous forme de `record` (§4.2) ; mappage de `User` sur `app_user` (§4.3, §5.3) ; `open-in-view: false` et conversion DTO dans la transaction (§4.3) ; hachage BCrypt, session `STATELESS`, CSRF désactivé (§3.4) ; secret JWT en clair dans `application.yaml` (§5.4) ; `ddl-auto: update` non versionné (§5.4) ; jeton de 24 h sans rôle ni rafraîchissement ni révocation, filtre ne capturant pas les exceptions (§5.4) ; gestion des erreurs ramenant tout à 400 (§5.4) ; conteneurisation orientée développement : `root`, identifiants en clair, `-DskipTests`, pas de cache Maven (§5.4) ; absence de CI, absence de pagination (§5.4) ; couverture de test très faible, un seul test unitaire (§5.1) ; frontend avec URL codée en dur, jeton en `localStorage`, pas de gestion des 401 (§4.8).

### C. `README.md` — à réécrire

Le `README.md` (51 lignes) est en retard sur le code :

- « Client Management (**Create, Read**) » → modification et suppression logique existent.
- « Claim Management (**Create**, …) » → les sinistres sont aussi consultables (par contrat, globalement, par identifiant).
- Aucune mention de : `GET /api/users` et du rôle `ADMIN`, du téléversement de justificatif, du *soft delete*, de la gestion des employés, de l'endpoint `PUT`, du compte `agent/agent123` (un seul compte de démonstration est documenté), du frontend Angular, de Docker/Docker Compose (alors que `Dockerfile` et `docker-compose.yml` existent), de la règle des 18 ans et des formats CIN/téléphone à 8 chiffres.
- Il renvoie à `application.yml` alors que le fichier s'appelle `application.yaml`.
- Le « Workflow » décrit un enregistrement manuel via `POST /api/auth/register`, alors que `DataInitializer` crée déjà les comptes au démarrage.

### D. `docs/architecture.md` — diagramme incomplet

Le diagramme de classes Mermaid omet : `Client.active` (le champ qui porte le *soft delete*), `Claim.documentPath` (le justificatif téléversé), et les champs `firstName`, `lastName`, `email` de `User`. Il est par ailleurs redondant avec la figure 2 du rapport, qui présente les mêmes lacunes.

### E. Anomalie de dépôt vérifiée : `uploads/` commité dans Git

Le `.gitignore` se termine par une ligne **encodée en UTF-16LE** au lieu d'UTF-8. Lecture octet à octet (`od -c`) de la fin du fichier :

```
u  \0   p  \0   l  \0   o  \0   a  \0   d  \0   s  \0   /  \0  \r  \0  \n  \0
```

Git lit donc une ligne composée de caractères alternés d'octets nuls, et **non** le motif `uploads/`. Conséquence vérifiée : `git ls-files uploads` renvoie deux fichiers, `uploads/8a6121b7-baef-4f35-a5b4-ea78b0fe511d.jpg` et `uploads/a9567aee-18ad-4a9b-acde-f76a8a07018c.jpg` (15 829 octets chacun). L'intention d'ignorer le dossier était présente, elle est simplement inopérante. À signaler comme défaut d'hygiène de dépôt (données de test versionnées), avec le correctif : réécrire la ligne en UTF-8.

### F. Défaut vérifié par le build : CSS tronqué

`frontend/src/app/features/dashboard/dashboard.component.css` compte **58 lignes** et se termine au milieu d'une déclaration, sans accolade fermante :

```css
.action-card {
  …
  transition: transform 0.2s, box-shadow      ← fichier coupé ici
```

Le build Angular émet l'avertissement `[css-syntax-error] Expected "}" to go with "{"` pointant `dashboard.component.css:59:0` et rappelant l'accolade ouverte en `49:13`. Le build **réussit** malgré tout, mais la règle `.action-card` est incomplète : le style de transition des cartes du tableau de bord n'est pas appliqué intégralement.

---

## 20. Synthèse des faiblesses vérifiées (pour la section « regard critique »)

Classées par gravité, chacune rattachée à un fait constaté :

**Sécurité**
1. `@EnableMethodSecurity` absent → 5 annotations `@PreAuthorize` inopérantes ; un `AGENT` peut `DELETE /api/clients/{id}` et `PUT /api/contracts/{id}`.
2. Secret JWT en clair et commité dans `application.yaml`.
3. Compte `admin/admin123` créé en dur à **chaque** démarrage, sans condition de profil.
4. Jeton stocké en `localStorage` côté client, sans expiration vérifiée ni gestion des 401.
5. Téléversement sans limite de taille, sans contrôle de type, sans analyse ; chemin non restitué mais fichier écrit sur disque.
6. `include-message: always` : les messages d'exceptions internes (dont SQL) sont renvoyés au client.
7. Image Docker exécutée en `root`, identifiants de base en clair dans `docker-compose.yml`.

**Robustesse / conception**
8. Toute exception remontée en **400** : pas de 404, 401, 403, 409 ; pas de format d'erreur normalisé.
9. Contrôle « client porteur de contrats non supprimable » **annoncé mais absent** ; dépendance injectée inutilisée.
10. `updateContract` n'écrase pas seulement ce qui est fourni : il écrit `coverageAmount`, `premiumAmount` et `endDate` même absents du corps → risque de `null` sur une colonne `nullable = false`.
11. Numéros de police et de sinistre générés par `currentTimeMillis() % 100000` → collision possible sur des colonnes `unique`.
12. Écriture du fichier avant la persistance → fichiers orphelins en cas d'échec.
13. Aucune pagination sur les listes ; `GET /api/contracts` renvoie tout, et le frontend l'utilise pour filtrer côté client.
14. Bean Validation non appliquée sur la route de création de sinistre (`@ModelAttribute` sans `@Valid`).
15. `DataInitializer` non conditionné à un profil ; `show-sql: true` en permanence.

**Qualité logicielle**
16. 1 test unitaire backend pour 19 règles métier ; aucun test de contrôleur, de sécurité ni d'intégration ; pas de JaCoCo.
17. Suite de tests frontend **non exécutable** : 8 fichiers `*.spec.ts` cassés (cf. §18).
18. Aucune CI dans le dépôt.
19. Schéma non versionné (`ddl-auto: update`), aucune migration.
20. Documentation de dépôt désalignée : `README.md`, `docs/architecture.md` et le brouillon de rapport lui-même (cf. §19).
21. Code mort et scories : imports inutilisés dans `MiniApiAssuranceApplication` (`Client`, `ClientRepository`, `Bean`, `LocalDate`), `@PreAuthorize` commenté, `@NotBlank` dupliqué, commentaire springdoc quadruplé dans le `pom.xml`, balises Maven vides, `existsByClientId` et `existsByContractId` inutilisés, `contract.spec.ts` orphelin.
22. `.gitignore` partiellement en UTF-16 → `uploads/` versionné (cf. §19-E).
23. Fichier CSS tronqué dans le dashboard (cf. §19-F).

---

## 21. Journal de vérification : ce qui a été exécuté, ce qui ne l'a pas été

### Vérifié par exécution dans ce dépôt

| Vérification | Commande | Résultat obtenu |
|---|---|---|
| Installation des dépendances frontend | `npm ci --no-audit --no-fund` | **Succès** — « added 473 packages in 9s » |
| Compilation du frontend | `npx ng build --configuration development` | **Succès** — « Application bundle generation complete. [5.003 seconds] », sortie dans `frontend/dist/frontend`, bundle initial 2,07 MB, 3 chunks paresseux (`client-detail`, `contract-detail`, `claim-detail`) ; **1 avertissement** `[css-syntax-error]` sur `dashboard.component.css:59:0` |
| Tests unitaires frontend | `npx ng test --watch=false` | **Échec de compilation** — 8 erreurs TypeScript (`TS2305` ×7, `TS2307` ×1) listées en §18 |
| Absence de sécurité par méthode | `grep -rn "EnableMethodSecurity" src/main/java/…` | **Aucune occurrence** |
| Inventaire des `@PreAuthorize` | `grep -rn "@PreAuthorize" controller/` | 5 actives + 1 commentée |
| Inventaire des routes | `grep -rn -E "@(Get\|Post\|Put\|Delete)Mapping\|@RequestMapping" controller/` | 17 points d'accès |
| Usage de `existsByClientId` | `grep -rn "existsByClientId" src/` | déclaration seule, aucun appel |
| Usage de `contractRepository` dans `ClientService` | `grep -n "contractRepository" service/ClientService.java` | 3 occurrences : champ, paramètre, affectation — **aucun appel de méthode** |
| Profondeur de l'historique Git | `git log --oneline --all \| wc -l` | 1 |
| Fichiers `uploads/` suivis par Git | `git ls-files uploads` | 2 fichiers `.jpg` |
| Encodage anormal du `.gitignore` | `tail -c 40 .gitignore \| od -c` | ligne `uploads/` en UTF-16LE |
| Troncature du CSS | `wc -l dashboard.component.css` + `sed -n '40,70p'` | 58 lignes, dernière déclaration inachevée |
| Métriques de taille | `find … \| xargs wc -l` | 1 811 lignes Java, 1 071 lignes TypeScript, 139 fichiers suivis |

### **Non vérifié** — à ne pas présenter comme testé

**Le backend n'a pas été compilé ni exécuté, et aucun test backend n'a été lancé.** Cause : aucun JDK dans l'environnement de travail (`java -version` → `command not found`), aucun Maven installé ni répertoire `~/.m2`, et **aucun accès réseau vers les dépôts nécessaires** — `https://repo1.maven.org` inaccessible (`curl: (35) OpenSSL SSL_connect: SSL_ERROR_SYSCALL`) et `apt-get update` en échec sur `deb.debian.org` (`Connection failed`), ce qui interdit d'installer un JDK. Le registre npm, lui, est joignable, d'où la vérification possible du frontend uniquement.

Par conséquent, les éléments suivants sont établis **par lecture du code et non par exécution** et doivent être présentés comme tels dans un rapport : le comportement HTTP réel de chaque point d'accès, les codes de statut effectivement renvoyés, l'effet de `@EnableMethodSecurity` manquant à l'exécution, la génération du schéma PostgreSQL par Hibernate, le démarrage de la pile Docker Compose, l'ordre d'exécution du `healthcheck`, la génération et la validation réelles d'un jeton JWT, et le passage du test `ClaimServiceTest`. Un rapport sérieux devrait indiquer que ces comportements ont été **vérifiés par l'auteur pendant le stage** (Swagger UI, `psql`, Docker Desktop — comme le rapport l'affirme en §5.1), et non par une exécution reproductible dans un environnement d'intégration continue, puisqu'il n'en existe pas.

---

## 22. Démarrage (procédure)

### Backend — exécution locale

```bash
# prérequis : JDK 17, PostgreSQL démarré
createdb assurance_db
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/assurance_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=admin
./mvnw spring-boot:run
```

### Backend — exécution conteneurisée

```bash
docker compose up --build     # db (postgres:16) puis api, une fois pg_isready satisfait
docker compose down           # arrêt ; le volume pgdata conserve les données
docker compose down -v        # arrêt et suppression des données
```

### Frontend

```bash
cd frontend
npm ci            # 473 paquets (vérifié)
npm start         # ng serve → http://localhost:4200 (origine autorisée par le CORS)
npm run build     # vérifié : succès, 1 avertissement CSS
npm test          # vérifié : échec, 8 fichiers de spec cassés
```

### Accès

| Ressource | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| Spécification OpenAPI | `http://localhost:8080/v3/api-docs` |
| Frontend | `http://localhost:4200` |
| Base de données | `localhost:5432` / `assurance_db` |

### Comptes de démonstration (créés au démarrage, développement local uniquement)

| Identifiant | Mot de passe | Rôle | Accès |
|---|---|---|---|
| `admin` | `admin123` | `ADMIN` | tout, y compris `GET /api/users` |
| `agent` | `agent123` | `AGENT` | clients, contrats, sinistres — **pas** `/api/users` |

### Parcours de démonstration recommandé

1. `POST /api/auth/login` avec `admin`/`admin123` → récupérer le jeton.
2. Bouton **Authorize** de Swagger, coller `Bearer <jeton>`.
3. `POST /api/clients` (CIN et téléphone à 8 chiffres, date de naissance assurant ≥ 18 ans) → 201.
4. `POST /api/contracts` sur ce client → 201, numéro de police généré, statut `ACTIVE`.
5. `POST /api/contracts/{id}/claims` sur le contrat `CT-2026-00001` (actif) → 201.
6. Même appel sur le contrat `CT-2026-00002` (`TERMINATED`) → **400** `"Cannot file a claim: Contract is not ACTIVE"` (règle RM13).
7. Appel avec une `claimDate` hors période → **400** `"Claim date must be within the contract coverage period"` (règle RM14).
8. Se déconnecter, se reconnecter en `agent`, appeler `GET /api/users` → **403** (seule restriction par rôle effective).
9. `DELETE /api/clients/{id}` → 204 ; `GET /api/clients` ne liste plus le client, mais `GET /api/contracts` renvoie toujours ses contrats (illustration de la limite du §20.9).

---

## 23. Vocabulaire du domaine

| Terme | Sens dans ce projet |
|---|---|
| Client | personne physique assurée, identifiée par un CIN à 8 chiffres |
| Contrat / police | garantie souscrite par un client ; `policyNumber` au format `CT-<année>-<n>` |
| Prime (`premiumAmount`) | montant payé par l'assuré en contrepartie de la garantie |
| Couverture (`coverageAmount`) | montant maximal garanti |
| Sinistre (`Claim`) | événement dommageable déclaré au titre d'un contrat ; `claimNumber` au format `CL-<année>-<n>` |
| Date de survenance (`claimDate`) | date du fait dommageable — doit tomber dans la période de couverture |
| Date de déclaration (`declarationDate`) | date d'enregistrement par le système |
| Résiliation (`TERMINATED`) | arrêt du contrat avant son terme — interdit toute déclaration |
| Archivage (*soft delete*) | passage de `Client.active` à `false` : le client disparaît des listes sans être effacé |
| CIN | Carte d'identité nationale tunisienne |
| Employé | utilisateur de l'application (`User`), de rôle `ADMIN` ou `AGENT` |

---

## 24. Recommandations de priorisation (si le rapport doit inclure des perspectives)

| Priorité | Action | Effort | Référence |
|---|---|---|---|
| 1 | Ajouter `@EnableMethodSecurity` et tester les refus par rôle | très faible | §20.1 |
| 2 | Corriger `.gitignore` (UTF-8) et retirer `uploads/` du versionnage | très faible | §19-E |
| 3 | Externaliser `jwt.secret` par variable d'environnement | très faible | §20.2 |
| 4 | Gestionnaires d'exceptions dédiés (404 / 401 / 403 / 409) + format RFC 9457 | faible | §20.8 |
| 5 | Implémenter le contrôle « client porteur de contrats non supprimable » (`existsByClientId` déjà déclarée) | faible | §20.9 |
| 6 | Conditionner `DataInitializer` à un profil `dev` | faible | §20.3 |
| 7 | Réparer ou supprimer les 8 fichiers `*.spec.ts` cassés, compléter `dashboard.component.css` | faible | §18, §19-F |
| 8 | Mettre à jour `README.md` et `docs/architecture.md` | faible | §19-C, §19-D |
| 9 | Corriger `updateContract` pour ne mettre à jour que les champs fournis et revalider les dates | faible | §20.10 |
| 10 | Pagination et filtrage des listes ; utiliser `GET /api/contracts/client/{id}` côté frontend | moyen | §20.13 |
| 11 | Migrations versionnées (Flyway/Liquibase) à la place de `ddl-auto: update` | moyen | §20.19 |
| 12 | Générations d'identifiants métier fiables (séquence ou compteur annuel) | moyen | §20.11 |
| 13 | Tests de contrôleur (`MockMvc`), tests de sécurité, JaCoCo, chaîne CI | moyen | §20.16, §20.18 |
| 14 | Workflow d'instruction des sinistres (transitions `PROCESSING`/`ACCEPTED`/`REJECTED`, remboursement) | élevé | §2 |
| 15 | Endpoint de restitution des justificatifs + volume persistant pour `uploads/` | moyen | §12 |
