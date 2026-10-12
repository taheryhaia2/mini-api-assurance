# Audit du dépôt et du cadre documentaire — préparation du rapport de stage

Auteur de l'audit : agent Arena (session `arena/0b6f71ae-mini-api-assurance`)
Date de l'audit : 12 octobre 2026
Périmètre : dépôt `taheryhaia2/mini-api-assurance`, branche `master` (commit `1a354ad`), branches distantes et PR n°1 consultées en lecture seule.

Ce document ne constitue pas le rapport. Il recense ce qui est établi, ce qui est contradictoire et ce qui manque.

---

## 1. Sources disponibles

| Source | Statut |
|---|---|
| Code backend (Spring Boot 3.5.16, Java 17) | Disponible, lu en intégralité pour les contrôleurs, services, sécurité, domaine, DTO, exceptions, configuration |
| Code frontend (Angular 21) | Disponible, routes, services, guards, intercepteur lus ; composants inspectés par leur présence |
| Configuration (`pom.xml`, `application.yaml`, `Dockerfile`, `docker-compose.yml`) | Disponible |
| Tests | 2 classes de test backend, 9 fichiers `.spec.ts` frontend (génération par défaut) |
| Historique | `master` : 36 commits, du 5 juillet 2026 au 30 juillet 2026 (auteur : `taher`). Clone local superficiel (1 commit visible) ; historique complet lu via l'API GitHub |
| Documentation du dépôt | `README.md`, `docs/architecture.md` (diagramme de classes), `frontend/README.md` (boilerplate Angular) |
| Brouillon de rapport | `docs/# Rapport de stage — Mini-API Assur.md` (61 Ko, voir section 4) |
| Guide officiel de rédaction ISIMG | **Absent** de l'environnement et du dépôt |
| Logos officiels | **Absents** de l'environnement ; des logos non documentés existent sur la branche de la PR n°1 (voir section 5) |
| Captures d'écran, schémas exportés | Aucun dans `master` |
| Sujet écrit du stage / convention de stage | **Absent** |
| Fiche de suivi, attestation, planning | **Absents** |

Limites d'environnement constatées :

- Java et Maven ne sont pas installés et Maven Central n'est pas accessible : le backend **n'a pas été compilé ni testé** dans cette session.
- Node.js est disponible : le frontend pourrait être compilé (registre npm autorisé), mais cela n'a pas été fait à ce stade.
- Aucun service PostgreSQL n'est disponible : l'API n'a pas été démarrée.

---

## 2. Fonctionnalités réellement implémentées (vérifiées dans le code)

### 2.1 Backend

**Points d'accès : 17** (et non 13 comme dans le brouillon) :

- Authentification (2) : `POST /api/auth/register`, `POST /api/auth/login`.
- Clients (5) : `POST`, `GET` (liste des clients actifs), `GET /{id}`, `PUT /{id}`, `DELETE /{id}` (archivage logique).
- Contrats (5) : `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `GET /client/{clientId}`.
- Sinistres (4) : `POST /api/contracts/{contractId}/claims` (multipart, avec justificatif facultatif), `GET /api/contracts/{contractId}/claims`, `GET /api/claims`, `GET /api/claims/{id}`.
- Utilisateurs (1) : `GET /api/users` (réservé à ADMIN).

**Entités** : `Client` (avec champ `active`, soft delete), `Contract`, `Claim` (avec `documentPath`, `claimNumber` unique), `User` (table `app_user`, avec prénom, nom, courriel). Énumérations : `ContractType` (AUTO, HOME, HEALTH, LIFE), `ContractStatus` (ACTIVE, EXPIRED, TERMINATED), `ClaimStatus` (SUBMITTED, PROCESSING, ACCEPTED, REJECTED), `Role` (ADMIN, AGENT).

**Règles métier effectivement présentes dans le code** :

| Règle | Emplacement | Vérifiée |
|---|---|---|
| CIN exactement 8 chiffres, unique | `ClientCreateDto` (regex), `ClientService.createClient` (`existsByCin`) | Oui |
| Téléphone exactement 8 chiffres | `ClientCreateDto` | Oui |
| Client âgé d'au moins 18 ans à la création | `ClientService.createClient` | Oui |
| Client archivé non modifiable | `ClientService.updateClient` | Oui |
| Suppression = archivage logique (`active = false`) | `ClientService.deleteClient` | Oui |
| Fin de contrat postérieure au début, **à la création uniquement** | `ContractService.createContract` | Oui (non revérifiée à la mise à jour) |
| Numéro de police généré par le serveur, statut initial ACTIVE | `ContractService` | Oui |
| Statut de contrat valide à la mise à jour | `ContractService.updateContract` | Oui |
| Sinistre refusé si contrat non ACTIVE | `ClaimService.createClaim` | Oui |
| Date de survenance dans la période de couverture | `ClaimService.createClaim` | Oui |
| Sinistre créé en SUBMITTED, remboursement à zéro | `ClaimService.createClaim` | Oui |
| Nom d'utilisateur et courriel uniques | `UserService.createUser` (lève `RuntimeException`) | Oui |
| Client lié à un contrat **non supprimable** | — | **Non** (voir section 3) |

**Téléversement de justificatif** : fichier copié dans le répertoire relatif `uploads/` sous un nom UUID, extension conservée, chemin enregistré dans `Claim.documentPath`. Aucun contrôle de type MIME ni de taille.

**Sécurité** :

- JWT HMAC, expiration 24 h, sujet = nom d'utilisateur (`JwtService`).
- Filtre `JwtAuthFilter` avant `UsernamePasswordAuthenticationFilter`.
- Mots de passe hachés BCrypt. Session `STATELESS`. CSRF désactivé. CORS limité à `http://localhost:4200`.
- Autorisation **par chemin** (active) dans `SecurityConfig` : `/api/users/**` réservé à ADMIN ; `/api/clients/**`, `/api/contracts/**`, `/api/claims/**` accessibles à ADMIN et AGENT.
- Autorisation **par méthode** (`@PreAuthorize`) : présente sur 5 méthodes actives (3 GET de sinistres, `DELETE /api/clients/{id}`, `PUT /api/contracts/{id}`), mais **inopérante**, car `@EnableMethodSecurity` n'existe nulle part dans le code.

**Données de démonstration** (`DataInitializer`) : comptes `admin/admin123` (ADMIN) et `agent/agent123` (AGENT), client « Ahmed Ben Ali », un contrat ACTIVE et un contrat TERMINATED.

**Gestion des erreurs** : `GlobalExceptionHandler` renvoie **400** pour toute `RuntimeException` (y compris « non trouvé ») et pour les erreurs de validation. Pas de 401/403/404/409 distincts.

### 2.2 Frontend (Angular 21)

**11 écrans / routes** (et non 4 comme dans le brouillon) : connexion, tableau de bord (Chart.js), liste des clients, détail d'un client, création de client, liste des contrats, détail d'un contrat (avec sinistres), liste des sinistres, détail d'un sinistre, formulaire de déclaration de sinistre (avec envoi multipart), gestion des employés (route `/users`, protégée par `roleGuard`).

**Mécanismes** : `authGuard`, `roleGuard`, `jwtInterceptor` (ajoute `Authorization: Bearer`), jeton stocké dans `localStorage`.

**Limites constatées** :

- URL de l'API **codée en dur** (`http://localhost:8080/...`) dans 6 services/composants.
- Aucun écran de modification ni de suppression de client ou de contrat (à vérifier dans les composants `client-detail` et `contract-detail` ; à confirmer par lecture complète).
- Fichiers `.spec.ts` : tests générés par défaut (« should create »). Leur exécution n'a pas été vérifiée.

### 2.3 Infrastructure

- `Dockerfile` en deux étapes (Maven 3.9.9 / Temurin 17, puis Temurin 17 JRE), exécution en `root`, tests ignorés au build.
- `docker-compose.yml` : service `db` (postgres:16, healthcheck `pg_isready`) et service `api` (dépendance `service_healthy`). Mots de passe en clair dans le fichier.
- **Aucun volume pour `uploads/`** : les justificatifs sont perdus si le conteneur est recréé.

---

## 3. Fonctionnalités partielles, incohérences et défauts

1. **Règle « client lié à un contrat non supprimable » : régression constatée.**
   - Le commit `1db5da3` (15 juillet) ajoute `contractRepository.existsByClientId` dans `deleteClient` et une `BusinessRuleException`.
   - Le commit `4a0dfb8` (29 juillet, « regles metier, soft delete ») remplace la suppression physique par un archivage logique et **supprime ce contrôle**.
   - Dans le code actuel, `existsByClientId` est déclarée mais **jamais appelée**, et `contractRepository` est injecté sans être utilisé dans `ClientService.deleteClient`.
   - Question à trancher par l'étudiant : abandon volontaire (l'archivage conserve les contrats) ou oubli ?

2. **Autorisation par rôle partiellement effective.** Par chemin : active. Par méthode : inopérante. Conséquence : un compte AGENT peut appeler `DELETE /api/clients/{id}` et `PUT /api/contracts/{id}`, malgré les `@PreAuthorize(hasRole('ADMIN'))` présents sur ces méthodes.

3. **Validation de la période de contrat à la mise à jour** : `updateContract` modifie `endDate` sans vérifier qu'elle reste postérieure à `startDate`.

4. **Numéros générés** : `CT-<année>-<millis % 100000>` et `CL-<année>-<millis % 100000>`. Risque de collision (contrainte d'unicité), non testé.

5. **Téléversement** : pas de contrôle de type ni de taille ; chemin relatif `uploads/` dépendant du répertoire de lancement.

6. **`.gitignore` encodé en UTF-16** (caractères NUL entre chaque lettre). Git ne l'interprète pas : `uploads/` n'est **pas ignoré**, et deux fichiers JPEG (`uploads/8a6121b7-….jpg`, `uploads/a9567aee-….jpg`, 15,8 Ko chacun) sont versionnés dans `master`. Leur contenu et leur origine (données de test ou pièces réelles) sont **à vérifier avant toute publication**.

7. **Secrets** : la clé JWT est écrite en clair dans `application.yaml` ; le mot de passe PostgreSQL par défaut est `admin` dans la configuration et dans `docker-compose.yml`.

8. **Schéma** : `spring.jpa.hibernate.ddl-auto=update`, aucune migration.

9. **Tests** :
   - `ClaimServiceTest` : **1 méthode de test** (contrat TERMINATED → `BusinessRuleException`), Mockito, sans base.
   - `MiniApiAssuranceApplicationTests` : test de chargement du contexte, nécessite PostgreSQL.
   - Aucun test de contrôleur, de sécurité, de mapper ni d'intégration. Aucune mesure de couverture.
   - Frontend : tests par défaut non exécutés.

10. **Documentation désalignée** : `README.md` mentionne `application.yml` (le fichier réel est `application.yaml`) et ne signale que le compte admin ; il ne mentionne ni Docker, ni le frontend, ni les sinistres avec justificatif.

11. **Qualité du `pom.xml`** : le commentaire « Documentation Swagger / OpenAPI » est répété quatre fois ; sans conséquence fonctionnelle, mais à nettoyer si le dépôt est montré.

---

## 4. Analyse du brouillon existant `docs/# Rapport de stage — Mini-API Assur.md`

Ce brouillon (environ 5 300 mots, 5 figures Mermaid, 6 tableaux) **contredit le code sur plusieurs points majeurs** et ne peut pas servir de base telle quelle :

| Affirmation du brouillon | Réalité constatée |
|---|---|
| 13 points d'accès | 17 |
| 4 écrans Angular ; « les écrans contrats et sinistres n'existent pas » | 11 écrans, dont contrats et sinistres |
| Règle RM9 (client lié à un contrat non supprimable) implémentée | Absente (régression, voir 3.1) |
| Pas de téléversement | Téléversement de justificatif présent |
| Autorisation par rôle « non effective » pour toutes les routes | Effective par chemin ; inopérante seulement par méthode |
| Comptes de démo : `admin` et `agent` (annexe B) | Exact dans le code, mais le README ne mentionne que `admin` |
| Frontend réalisé « en fin de stage » | Commits frontend du 21 au 30 juillet ; le frontend occupe la deuxième moitié du mois |
| Formation : « Licence Fondamentale en Informatique — Génie Logiciel (FIGL), première année » ; « stage d'initiation » ; « quatre semaines » | Incompatible avec la formation indiquée dans votre demande (cycle ingénieur, FGL1 au moment du stage) |
| Semaine 1 à 4 avec découpage précis ; « Trame de soutenance » ; `NOTES_MISE_EN_PAGE_WORD.md` ; `TRAME_SOUTENANCE.md` | Ces fichiers n'existent pas dans le dépôt : contenu non vérifiable |
| Encadrante : Mme Faten Kardous, *Lead Developer* | Nom absent du dépôt : non vérifiable |
| Vermeg : fondée en 1993 en Tunisie sous le nom BFI, détachée en 2002, siège à Amsterdam | 1993, Amsterdam et origine tunisienne confirmés par des profils tiers (Tracxn, Bitscale, ZoomInfo). Détachement de 2002 et nom BFI **non confirmés** ; source citée (Wikipédia, site vermeg.com) à vérifier directement |

Autres observations sur le brouillon : ses références bibliographiques (RFC, documentations Spring, Docker, PostgreSQL) n'ont pas été vérifiées une à une ; ses notes internes (« Notes de conversion », « Note de volumétrie ») doivent être retirées de toute version finale.

---

## 5. Pull request n°1 et branches distantes (lecture seule)

- **PR n°1** « docs: rapport de stage ISIMG (Word + LaTeX) et dossier technique vérifié », ouverte le 3 octobre 2026, depuis la branche `arena/01a0ff05-mini-api-assurance`, **non fusionnée**.
- Cette branche contient : `docs/Rapport_de_stage_Mini-API_Assurance.docx` (257 Ko), `.tex`, `.md`, `docs/DOSSIER_PROJET.md` (66 Ko), 5 figures PNG (`docs/figures/`), et 3 logos (`docs/logos/`).
- Sa description indique que le guide n'était pas accessible dans cet environnement et que le `.docx` et le `.tex` n'ont **pas été compilés ni rendus** (aucun LibreOffice ni moteur TeX). Elle corrige le brouillon sur 11 points, dont plusieurs des points listés en section 4.
- **Je n'ai ni fusionné, ni modifié cette PR ni sa branche.** Son contenu n'a pas été relu en détail dans cet audit ; il devra l'être avant toute réutilisation.
- Autres branches distantes : `feature/frontend-auth`, `feat/translation-clean`, plusieurs `arena/*`. Non analysées.

### Logos trouvés sur la branche de la PR (non validés)

| Fichier | Dimensions | Constat |
|---|---|---|
| `logo-vermeg.png` | 300 × 300 px | Wordmark « VERMEG » sur fond blanc, fond carré. Résolution faible pour une couverture imprimée. |
| `logo-isimg.jpg` | 370 × 250 px | Logo ISIMG avec texte arabe, JPEG. Résolution faible. |
| `logo-universite-gabes.png` | 391 × 358 px | Non examiné visuellement dans cet audit. |

Provenance non documentée : je ne peux pas affirmer qu'il s'agit des logos officiels. L'accès internet de cet environnement est limité à GitHub, npm et PyPI : je ne peux donc pas télécharger les logos depuis les sites institutionnels.

---

## 6. Vérification des informations sur VERMEG

- Confirmé par des profils tiers (non officiels) : fondation en 1993, siège à Amsterdam, origine tunisienne.
- Non confirmé : détachement de BFI en 2002 ; organigramme ; service « Insurance Market Operations ».
- Information à connaître : un communiqué de presse de novembre 2024 annonce la cession de la division RegTech « Agile » à Regnology. À vérifier sur les sources officielles avant d'en parler dans le rapport.
- Une source officielle (site vermeg.com, rubrique « À propos ») doit être consultée et citée avant la rédaction du chapitre 1.

---

## 7. Informations manquantes ou à valider par l'étudiant

Bloquantes :

1. **Le guide officiel de rédaction** (PDF ou DOCX) : sans lui, la structure, la pagination, les marges et les règles de bibliographie ne peuvent pas être appliquées.
2. **Les logos officiels** haute résolution de l'ISIMG, de l'Université de Gabès et de VERMEG.
3. **Le statut exact** : « stage ouvrier » ou « stage technicien », et la terminologie utilisée par le guide.
4. **La formation** : le brouillon indique « Licence FIGL, première année ». Votre demande indique « cycle ingénieur, FGL1 au moment du stage ». Un seul des deux est juste.
5. **L'encadrement** : nom et fonction de l'encadrante entreprise, nom de l'encadrant académique, adresse du site Vermeg.
6. **La chronologie réelle** : ce qui a été fait semaine par semaine. L'historique git commence le 5 juillet et finit le 30 juillet. Les commits ne racontent pas la démarche (tests, difficultés, décisions).
7. **Les difficultés effectivement rencontrées** et les solutions retenues, avec leur cause. Le brouillon en liste sept : il faut confirmer lesquelles sont réelles.
8. **Le statut de la règle RM9** (abandon volontaire ou oubli, voir 3.1).
9. **Les résultats** : tests réellement exécutés par l'étudiant, démonstrations, éventuelles captures d'écran prises sur son poste.

Secondaires :

10. Année universitaire (le brouillon indique 2025/2026).
11. Autorisation de citer le nom de l'entreprise, le dépôt GitHub et le nom des comptes de démonstration dans le rapport.
12. Les deux fichiers JPEG de `uploads/` : confirmer qu'ils ne contiennent pas de données personnelles avant de les conserver ou de les publier.
13. Sort de la PR n°1 : réutiliser, remplacer ou ignorer.

---

## 8. Recommandations avant la rédaction

- Ne pas reprendre le brouillon existant tel quel : il contredit le code sur les points majeurs listés en section 4.
- Ne pas afficher les 17 points d'accès et les 11 écrans comme « réalisés » sans les avoir vérifiés à l'exécution.
- Signaler dans le rapport la régression RM9 et l'autorisation inopérante par méthode comme des limites constatées, sans les présenter comme des fonctionnalités.
- Pour les captures d'écran et la vérification du fonctionnement, il faudra soit un environnement avec Java 17 et PostgreSQL, soit des captures prises par l'étudiant.
- Ne rien présenter comme exécuté tant qu'il ne l'a pas été.

---

## 9. Mise à jour (second tour)

### 9.1 Éléments ne pouvant pas être exploités

- **Guide officiel de l'ISIMG : absent.** Le message annonce un guide fourni, mais aucun fichier n'est présent (ni dans `/home/user/uploads/`, qui n'existe pas, ni ailleurs dans l'environnement). Les consignes du message (police, interligne, marges, barème) ont été prises en compte, mais la structure exacte, la règle de comptage des pages et les exigences de bibliographie du guide ne sont pas vérifiées.
- **Logos : fichiers absents.** Le message mentionne `image-1.png`, `image-2.png` et `image-3.png`, mais aucun de ces fichiers n'est présent dans l'environnement. Les trois images apparaissent dans le message, mais elles ne sont pas enregistrées dans l'espace de travail et ne peuvent donc pas être insérées dans le document.

### 9.2 Informations confirmées par l'utilisateur (message du second tour)

- Étudiant : Taher Yahia (orthographe confirmée par l'utilisateur ; « Yahya » était une faute de frappe). Établissement : ISIMG, Université de Gabès.
- Formation au moment du stage : FGL1 (1re année du cycle ingénieur en génie logiciel). Formation actuelle : FGL2.
- Entreprise : VERMEG. Période : du 1er au 31 juillet 2026 inclus. Type de stage : **stage technicien**.
- Encadrante en entreprise : Mme Faten Kardous (confirmée par l'utilisateur, non vérifiable dans le dépôt).
- Encadrant académique : **non renseigné**. Ne pas l'inventer.

Incohérence à signaler : le brouillon indique « Licence FIGL, première année » ; le message et la première demande indiquent FGL1 du cycle ingénieur. Retenir la version de l'utilisateur.

### 9.3 Barème

Le barème du second tour totalise 20 points : Forme 6 (maîtrise du logiciel 4, niveau linguistique 2) et Fond 14 (mise en contexte 2, clarté des objectifs 2, description des tâches 3, analyse critique 2, intérêt pour la formation 3, compétences techniques 2). Ce barème diffère de celui de la première demande (Forme 4, incluant la mise en contexte ; Fond 16). Retenir le barème le plus récent, à confirmer à partir du guide.

### 9.4 Chronologie observable (historique GitHub)

- Dépôt GitHub créé le **6 juillet 2026** (07:57 UTC). Premier commit (`chore: init spring boot project via spring initializr`) le **5 juillet 2026** à 14:03 UTC, donc enregistré localement avant la création du dépôt distant.
- Commits par jour sur `master` : 5 juillet (2), 6 juillet (4), 7 juillet (4), 8 juillet (4), 13 juillet (5), 15 juillet (4), 21 juillet (1), 22 juillet (5), 29 juillet (2), 30 juillet (5). Total : 36 commits.
- Premier commit de la semaine 1 : modélisation (entité `Client`, configuration PostgreSQL, CRUD client). Cohérent avec la semaine 1 décrite par l'utilisateur, mais la modélisation retenue avec l'encadrante n'est pas traçable dans le dépôt (pas de document de conception daté).
- **Postman** : aucune collection Postman dans le dépôt. L'usage de Postman n'est donc pas démontré par le dépôt.
- **Tests dans le dépôt** : 2 classes de test backend (un test unitaire à une méthode, un test de contexte nécessitant PostgreSQL), 9 fichiers `.spec.ts` générés par défaut côté frontend. Aucun résultat de test conservé dans le dépôt.

### 9.5 Exécution de l'application

Java et Maven ne sont pas installés dans cet environnement : l'API n'a pas pu être démarrée ni testée ici. Aucune capture d'écran réelle n'a pu être produite. Le rapport ne doit pas affirmer une exécution de test précise sans preuve fournie par l'étudiant.

### 9.6 Informations personnelles encore nécessaires

1. Nom de l'encadrant académique (et fonction, si le guide l'exige).
2. Preuves d'exécution : captures d'écran prises sur votre poste, sortie de tests, collection Postman éventuelle.
3. Difficultés réellement rencontrées et solutions appliquées (le brouillon en liste sept ; seules celles confirmées seront retenues).
4. Répartition réelle des tâches entre la semaine 2 et la semaine 4.
5. Adresse du lieu de stage, si le guide l'exige sur la page de garde.

---

## Mise à jour (Phases C à E)

**Rapport produit** : `Rapport_Stage_Taher_Yahia.docx` (généré par `build_report.py`, figures par `build_figures.py`). Voir `README.md`.

**Vérifications faites sur le dépôt (confirmées)**
- Historique GitHub (API) : 36 commits sur `master`, du 5 au 30 juillet 2026 ; dépôt créé le 6 juillet 2026. Commits cités : 4644688 (7 juillet, HashMap), 1db5da3 (15 juillet, DELETE avec contrôle), 4a0dfb8 (29 juillet, soft delete).
- Versions : Spring Boot 3.5.16, springdoc 2.7.0, JJWT 0.12.6, Angular ^21.2.0, Chart.js ^4.5.1, PostgreSQL 16, images Docker maven:3.9.9-eclipse-temurin-17 / eclipse-temurin:17-jre.
- 17 points d'accès ; 11 routes Angular ; 10 règles métier appliquées, RM11 non appliquée.
- Sécurité : `/api/auth/**` permitAll ; `/api/users/**` ADMIN ; clients/contrats/sinistres ADMIN ou AGENT ; `@PreAuthorize` sans `@EnableMethodSecurity` ; CSRF désactivé ; session STATELESS ; BCrypt ; jeton 24 h. Le `PUT /api/contracts/{id}` est annoté ADMIN mais la règle de chemin autorise AGENT.
- Tests présents : un test unitaire (`ClaimServiceTest`, cas TERMINATED) et `contextLoads`. Aucun test exécuté dans ce travail.
- `.gitignore` : ASCII (`HELP.md`), pas UTF-16 ; `uploads/` contient deux `.jpg` suivis par Git (contenu non examiné).
- Secrets présents dans `application.yaml` (clé JWT) et `docker-compose.yml` (mots de passe). Non reproduits dans le rapport.
- Dockerfile sans `USER` (conteneur exécuté en root) ; `-DskipTests` au build.

**Corrections par rapport au brouillon de cette session**
- Retiré : l'affirmation « .gitignore en UTF-16 » (fausse) ; la mention de Postman (non vérifiée) ; « cycle ingénieur » ; les URL de sources non vérifiées.
- Sources externes retenues : communiqué Nordic Capital (novembre 2024), Crustdata, RocketReach (fondation 1993, siège Amsterdam). Non officielles.

**Nouveaux constats sécurité** : `POST /api/auth/register` public et acceptant `role` (non exécuté, déduit du code) ; suppression d'un client sans contrôle des contrats (RM11) ; annotations `@PreAuthorize` inopérantes.

**Pour la suite**
1. Guide ISIMG (absent du système) : vérifier stage type, barème, pages, en-tête/pied, page de garde, logos.
2. Confirmer : nom et fonction de l'encadrante en entreprise (Mme Faten Kardous, tirée du brouillon existant), année universitaire, FGL1, statut « stage technicien ».
3. Compléter les sections `[À COMPLÉTER]` : difficultés réellement rencontrées, tests manuels, bilan personnel, correspondance avec la formation.
4. Logos : non enregistrés dans le dossier ; les emplacements sont des cadres à remplacer par les logos fournis par l'étudiant ou officiels.
5. Longueur estimée ~17 pages de corps, à réduire si le guide impose 15 pages.
6. Retirer ou anonymiser les fichiers `uploads/` du dépôt avant remise publique (à décider avec l'étudiant).
