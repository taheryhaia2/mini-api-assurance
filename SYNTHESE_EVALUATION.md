# Synthèse d'auto-évaluation — grille ISIMG

Auto-relecture du document `RAPPORT_DE_STAGE.md` au regard des critères d'évaluation des stages de l'ISIMG (grille forme / fond). Objectif : identifier les points forts, les risques de perte de points et les corrections à effectuer avant impression.

> **Contexte** : stage d'initiation de première année du cycle d'ingénieur en Génie Logiciel (FIGL1). Étudiant : Taher Yahia — taher.yahia@isimg.tn — 58 780 980. Encadrante unique : Mme Faten Kardous (Vermeg). Il n'y a pas d'encadrant académique pour ce stage.

---

## 1. Évaluation de la forme

| Critère | État | Justification | Action requise |
|---|---|---|---|
| Maîtrise de l'outil de rédaction — sommaire | **Conforme après conversion** | Structure de titres hiérarchisée, table des matières listée dans le Markdown | Générer une table des matières **automatique** dans Word et supprimer la liste manuelle |
| Maîtrise de l'outil — liste des figures | **Conforme après conversion** | 5 figures identifiées et listées | Générer la liste via l'outil « Table des illustrations » |
| Maîtrise de l'outil — liste des tableaux | **Conforme après conversion** | 7 tableaux identifiés et listés | Idem, avec l'étiquette « Tableau » |
| Légendes des figures | **Conforme** | Légendes présentes et placées **sous** chaque figure | Utiliser le champ Légende de Word pour la numérotation automatique |
| Légendes des tableaux | **À faire en conversion** | Les légendes sont rédigées **au-dessus** de chaque tableau dans le Markdown | Convertir en champs Légende positionnés au-dessus |
| Citation des figures/tableaux dans le texte | **Conforme** | Les 5 figures et les 7 tableaux sont référencés dans le corps | Revérifier après toute coupe |
| Numérotation décimale des titres | **Conforme** | Format 1. / 1.1. / 1.1.1. respecté | Lier la numérotation aux styles Titre dans Word |
| Bibliographie / webographie | **Partiellement conforme** | 14 références, format homogène | **Risque** : [9] à [14] non citées dans le texte — les citer dans le chapitre 4 ou les retirer |
| Justification du texte | **À faire en conversion** | Non applicable en Markdown | Justifier tout le corps dans Word |
| Police, interligne, marges | **À faire en conversion** | Consignes rappelées en tête du rapport et détaillées dans `NOTES_MISE_EN_PAGE_WORD.md` | Appliquer : Times New Roman 12, interligne 1,5, marges 2,5/2,5/3/2,5 |
| En-tête et pied de page | **À faire en conversion** | Contenu proposé : « Mini-API Assurance — Vermeg » (33 car.) + « Taher Yahia » ; numéro de page | Configurer les sections Word |
| Absence de soulignement, titres non capitalisés | **Conforme** | Emphase en gras/italique uniquement, titres en casse normale | Vérifier après conversion |
| Niveau linguistique | **Bon** | Français professionnel, phrases courtes, vocabulaire métier employé correctement, pas de ton commercial | Relecture orthographique manuelle obligatoire (le correcteur ne détecte pas tout) |
| Mise en contexte | **Bon** | Introduction complète : cadre institutionnel, entreprise, problématique, objectifs, annonce du plan ; chapitre 1 dédié | — |
| Clarté des objectifs | **Bon** | 6 objectifs opérationnels numérotés en introduction ; périmètre et hors-périmètre explicités en 2.4 | — |

**Estimation forme : niveau attendu atteint sous réserve de la conversion Word et de la correction bibliographique.**

---

## 2. Évaluation du fond

| Critère | État | Justification | Risque |
|---|---|---|---|
| Description des tâches réalisées | **Solide** | Chapitre 4 complet : stack justifiée, backend, persistance, sécurité, 13 endpoints, 10 règles métier, OpenAPI, Docker, frontend ; chaque affirmation est adossée à un élément vérifiable du dépôt | Faible |
| Aspect critique | **Point fort** | Section 5.4 entièrement dédiée, avec 9 limites nommées, expliquées et hiérarchisées ; critique reprise en 6.4 et en conclusion | Très faible — c'est l'atout principal du rapport |
| Difficultés rencontrées | **Solide** | Tableau 5 avec 7 difficultés, leur analyse et la solution retenue ; une difficulté approfondie en commentaire | Faible |
| Intérêt par rapport à la formation | **Solide** | Section 6.3 met en relation explicite les modules POO, bases de données et algorithmique avec les travaux réalisés ; tableau 6 associe chaque compétence à un module | Faible |
| Acquisition de compétences techniques | **Solide** | Tableau 6 avec niveau atteint honnête (Consolidé / Nouveau / Initié), et trois acquis structurants développés en prose | Faible |
| Dépassement de la simple description | **Atteint** | Chaque choix technique est justifié (BigDecimal, EnumType.STRING, LAZY, DTO, STATELESS, CSRF, multi-stage), avec avantages et contreparties | Faible |
| Honnêteté du périmètre | **Point fort** | Le frontend est présenté quatre fois comme une initiative personnelle partielle ; Docker n'est jamais présenté comme un dispositif de production | Très faible |
| Cohérence code ↔ rapport | **Vérifiée** | Audit complet consigné dans `INVENTAIRE_REPO.md` ; aucune fonctionnalité absente du dépôt n'est revendiquée | Très faible |

---

## 3. Points forts à valoriser en soutenance

1. **La section critique est réellement critique.** Le rapport admet que les annotations `@PreAuthorize` sont inopérantes, que la gestion d'erreurs est trop grossière, que la couverture de test est très faible et que le secret JWT est en clair. C'est précisément ce que la grille ISIMG valorise sous « aspect critique ».
2. **Chaque choix technique est justifié, pas seulement énoncé.** `BigDecimal` plutôt que `double`, `EnumType.STRING` plutôt qu'ordinal, DTO plutôt qu'entités, désactivation raisonnée du CSRF, construction d'image en deux étapes.
3. **Le périmètre est délimité avec honnêteté.** Aucune confusion entre ce que l'entreprise a demandé et ce qui relève de l'initiative personnelle.
4. **Le lien technique ↔ métier est maintenu.** Les règles métier ne sont pas présentées comme des contrôles arbitraires mais rattachées à leur sens assurantiel.
5. **Aucun ton marketing.** Pas de superlatif, pas de « solution innovante », pas de liste de technologies sans explication.
6. **Vocabulaire du domaine maîtrisé** — sinistre, police, prime, couverture, statut d'instruction — ce qui compte face à une encadrante du département assurance.

---

## 4. Risques de perte de points et corrections

### Priorité haute — à traiter avant impression

| # | Risque | Correction |
|---|---|---|
| R1 | **Références [9] à [14] non citées dans le texte** — rompt la règle de cohérence bibliographique | Soit insérer les citations dans le chapitre 4 (documentation Spring Boot en 4.2, Spring Security en 4.4, Spring Data JPA en 4.3, OpenAPI en 4.6, Angular en 4.8, PostgreSQL en 4.3), soit supprimer ces six entrées |
| R2 | **Placeholders `[À COMPLÉTER]` restants** — il n'en reste que deux : l'entité et l'adresse exactes de Vermeg (page de garde) et les données entreprise en 1.1 | Renseigner ou supprimer les lignes concernées. Aucun `[À COMPLÉTER]` ne doit subsister dans la version imprimée |
| R3 | **Blocs de service non supprimés** — l'encadré « Notes de mise en page » en tête et la « Note de volumétrie » en fin de document ne font pas partie du rapport | Les supprimer lors de la conversion Word |
| R4 | **Figures Mermaid non converties** — le code Mermaid n'est pas lisible dans Word | Exporter chaque figure en PNG 2x ou SVG depuis mermaid.live et l'insérer comme image |
| R5 | **Page de garde non conforme au modèle officiel** | Remplacer par le modèle téléchargeable depuis le compte ISIMG, en y reportant les informations fournies |

### Priorité moyenne

| # | Risque | Correction |
|---|---|---|
| R6 | **Volume estimé à 16-17 pages**, soit légèrement au-dessus de la « quinzaine » demandée | Mesurer le nombre réel de pages après conversion, puis appliquer les 5 coupes hiérarchisées listées en fin de rapport jusqu'à revenir sous 15 pages. **Ne jamais raccourcir la section 5.4 ni le tableau 6** |
| R7 | **Typographie française** — espaces insécables absentes devant `;` `:` `?` `!` `%` | Rechercher/remplacer dans Word, ou activer la correction automatique française |
| R8 | **Liens hypertextes bleus soulignés** dans la bibliographie — contredit la consigne « pas de soulignement » | Supprimer les liens hypertextes et appliquer le style Normal |
| R9 | **Extraits de code potentiellement mal rendus** | Appliquer Consolas/Courier New 9-10 pts, interligne simple, encadré discret ; vérifier qu'aucun extrait n'est coupé entre deux pages |
| R10 | **Tableaux coupés en fin de page** | Activer « Répéter les lignes d'en-tête » et « Lignes solidaires » |

### Priorité basse — améliorations facultatives

| # | Amélioration | Bénéfice |
|---|---|---|
| R11 | Ajouter des captures d'écran réelles en annexe (Swagger UI, réponse d'erreur JSON, `docker compose up`, écran Angular) | Renforce la crédibilité ; les numéroter en Figures 6, 7… et les citer dans le texte |
| R12 | Insérer un diagramme de Gantt de la planification des 4 semaines en section 1.2 | Valorise le critère « mise en contexte » et l'organisation du travail |
| R13 | Ajouter un cas d'utilisation UML en section 2.2 | Renforce la partie analyse du besoin |

---

## 5. Corrections recommandées dans le dépôt (hors rapport)

Ces actions ne concernent pas le document mais peuvent être mentionnées en soutenance comme « corrections apportées après relecture », ce qui est valorisant. Elles doivent être faites **avant** la soutenance, ou pas du tout — mais dans le premier cas, le rapport devra être mis à jour en conséquence.

| Action | Effort | Impact en soutenance |
|---|---|---|
| Ajouter `@EnableMethodSecurity` sur `SecurityConfig` et vérifier les refus par un test | 30 min | Fort — corrige le défaut le plus visible |
| Externaliser `jwt.secret` via `${JWT_SECRET:...}` dans `application.yaml` | 10 min | Fort — supprime un secret du dépôt |
| Ajouter un `@ExceptionHandler(NotFoundException.class)` renvoyant 404 | 20 min | Moyen |
| Mettre à jour le `README.md` : Docker, frontend, opérations réellement disponibles, nom exact `application.yaml` | 30 min | Moyen — le jury peut consulter le dépôt |
| Refermer le bloc Mermaid non terminé dans `docs/architecture.md` | 2 min | Faible mais visible |
| Corriger ou supprimer `frontend/src/app/app.spec.ts`, qui teste encore le gabarit par défaut « Hello, frontend » | 10 min | Faible |

> **Attention à la cohérence** : si ces corrections sont appliquées, mettre à jour les sections 4.4, 5.4 et 6.4 du rapport en conséquence. Un rapport qui décrit un défaut corrigé entre-temps est aussi problématique qu'un rapport qui masque un défaut existant. À l'inverse, si le temps manque, **ne rien corriger et conserver le rapport tel quel** : il est exact et sa critique est assumée.

---

## 6. Verdict global

Le rapport répond aux exigences de fond de la grille ISIMG. Sa force principale réside dans la qualité de l'analyse critique et dans la traçabilité entre les affirmations et le code réel du dépôt. Sa faiblesse actuelle est de forme et relève entièrement de l'étape de conversion vers Word.

**Séquence de finalisation recommandée :**

1. Traiter les risques R1 à R5 (priorité haute).
2. Convertir vers Word en suivant `NOTES_MISE_EN_PAGE_WORD.md`.
3. Mesurer le nombre de pages et appliquer les coupes si nécessaire (R6).
4. Traiter R7 à R10.
5. Relire intégralement à voix haute (détecte les phrases mal construites).
6. Mettre à jour la table des matières et les listes de figures et de tableaux (`Ctrl + A` puis `F9`).
7. Exporter en PDF et vérifier page par page.
8. Préparer la soutenance à partir de `TRAME_SOUTENANCE.md`.
