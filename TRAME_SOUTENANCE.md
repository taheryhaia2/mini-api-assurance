# Trame de soutenance — Mini-API Assurance

- **Candidat** : Taher Yahia — cycle d'ingénieur Génie Logiciel (FIGL1), ISIMG, Université de Gabès — taher.yahia@isimg.tn
- **Stage** : Vermeg, 1er — 31 juillet 2026, encadrante Mme Faten Kardous (Lead Developer, Insurance Market Operations)
- **Format visé** : 12 diapositives, exposé de 8 à 10 minutes, suivi de questions
- **Règle de conduite** : une idée par diapositive, peu de texte affiché, pas de lecture des diapositives, une démonstration courte si l'équipement le permet

---

## 1. Plan des diapositives

### Diapositive 1 — Page de titre
**Contenu affiché** : titre du travail, nom, formation (cycle d'ingénieur Génie Logiciel, 1re année), entreprise, encadrante, année universitaire, logos ISIMG et Vermeg.
**Durée** : 20 s.

### Diapositive 2 — Contexte
**Contenu affiché** : ISIMG, cycle d'ingénieur Génie Logiciel / stage d'initiation de 4 semaines ; Vermeg, éditeur de logiciels pour les services financiers et l'assurance ; département Insurance Market Operations.
**Durée** : 45 s.

### Diapositive 3 — Sujet et problématique
**Contenu affiché** : « Exposer de manière sûre, cohérente et documentée les opérations de gestion d'un portefeuille d'assurance élémentaire » + les trois objets métier (client, contrat, sinistre).
**Durée** : 45 s.

### Diapositive 4 — Périmètre
**Contenu affiché** : deux colonnes. *Demandé* : API REST, persistance, sécurité, documentation, environnement reproductible. *Non demandé / initiative personnelle* : frontend Angular. *Hors périmètre* : instruction des sinistres, production.
**Durée** : 45 s.

### Diapositive 5 — Modèle de domaine
**Contenu affiché** : figure 2 du rapport (diagramme de classes).
**Durée** : 50 s.

### Diapositive 6 — Architecture en couches
**Contenu affiché** : figure 1 du rapport.
**Durée** : 60 s.

### Diapositive 7 — Sécurité JWT
**Contenu affiché** : figure 3 du rapport (diagramme de séquence).
**Durée** : 60 s.

### Diapositive 8 — Règles métier (cœur de la démonstration)
**Contenu affiché** : 3 règles seulement — sinistre interdit sur contrat non actif ; date de sinistre dans la période de couverture ; client non supprimable s'il a des contrats. Capture de la réponse d'erreur JSON.
**Durée** : 70 s.

### Diapositive 9 — Documentation OpenAPI
**Contenu affiché** : capture d'écran de Swagger UI avec le bouton « Authorize ».
**Durée** : 40 s.

### Diapositive 10 — Conteneurisation
**Contenu affiché** : figure 4 du rapport + la mention « `docker compose up` : API + base en deux commandes ».
**Durée** : 50 s.

### Diapositive 11 — Initiative personnelle : frontend Angular
**Contenu affiché** : capture de la page de connexion et de la liste des clients + une ligne « réalisé / non réalisé ».
**Durée** : 45 s.

### Diapositive 12 — Regard critique, apports, perspectives
**Contenu affiché** : trois limites majeures, trois apports, trois perspectives. Pas de liste plus longue.
**Durée** : 70 s.

---

## 2. Texte oral suggéré (≈ 9 minutes)

**[D1 — Titre]**
Bonjour. Je m'appelle Taher Yahia, élève ingénieur en première année de Génie Logiciel à l'ISIMG. Je vais vous présenter le travail que j'ai réalisé pendant mon stage d'initiation chez Vermeg, du 1er au 31 juillet 2026, sous l'encadrement de Mme Faten Kardous. Le sujet : une mini-API REST de gestion d'assurance.

**[D2 — Contexte]**
Vermeg est un éditeur de solutions logicielles pour les services financiers : la banque, les marchés de capitaux et l'assurance. J'ai été accueilli dans le département Insurance Market Operations, qui travaille sur le segment assurance. Cela explique le sujet qui m'a été confié : manipuler les objets métier fondamentaux de ce domaine.

**[D3 — Sujet et problématique]**
Un système d'assurance repose sur trois objets liés : un client, qui souscrit des contrats, sur lesquels peuvent être déclarés des sinistres. La question à laquelle j'ai dû répondre est la suivante : comment exposer les opérations sur ces objets de manière sûre, cohérente et documentée, en garantissant que les règles qui les lient soient toujours respectées, quel que soit le client qui appelle le service ?

**[D4 — Périmètre]**
Je précise d'emblée le périmètre. Ce qui m'a été demandé, c'est le backend : l'API, la base de données, la sécurité, la documentation. J'y ai ajouté la conteneurisation pour rendre l'environnement reproductible. En fin de stage, j'ai développé une interface Angular : ce n'était pas une demande de l'entreprise, c'est une initiative personnelle d'apprentissage, et elle est partielle. J'y reviendrai honnêtement à la fin.

**[D5 — Modèle de domaine]**
Voici le modèle. Quatre entités persistantes. Client, avec ses données d'état civil et son CIN unique. Contrat, avec un numéro de police, un type — auto, habitation, santé, vie —, une période de validité, des montants et un statut. Sinistre, rattaché à un contrat. Et une entité Utilisateur, séparée du domaine, dédiée à l'authentification. Deux choix méritent une explication : les montants sont en `BigDecimal` et non en `double`, parce qu'un flottant binaire introduit des erreurs d'arrondi inacceptables sur des montants financiers ; et les énumérations sont stockées en chaînes de caractères, pas par leur position, sinon ajouter une valeur au milieu corromprait les données existantes.

**[D6 — Architecture]**
L'application suit une architecture en couches. La requête traverse d'abord la chaîne de sécurité, arrive au contrôleur, qui ne fait qu'exposer le HTTP et déléguer. Le service porte les règles métier et la transaction. Le repository dialogue avec PostgreSQL via JPA. Entre le contrôleur et l'extérieur, je n'expose jamais les entités : j'utilise des objets de transfert dédiés. Cela évite de divulguer des champs internes comme le mot de passe haché, et cela me permet d'avoir des contrats d'entrée différents — par exemple, le DTO de mise à jour d'un client ne contient pas le CIN, ce qui interdit techniquement de modifier un identifiant national après création.

**[D7 — Sécurité]**
La sécurité repose sur des jetons JWT. L'utilisateur s'authentifie une fois, reçoit un jeton signé valable 24 heures, puis le présente dans l'en-tête Authorization à chaque requête. Le serveur ne conserve aucune session : il vérifie la signature et l'expiration à chaque appel. Les mots de passe sont hachés avec BCrypt. J'ai désactivé la protection CSRF, et je peux le justifier : cette attaque exploite l'envoi automatique des cookies par le navigateur, or je n'utilise pas de cookie, le jeton est transmis explicitement.

**[D8 — Règles métier]**
C'est le cœur du travail. La valeur de l'API n'est pas dans le fait de lire et d'écrire des données, c'est dans les règles que le serveur fait respecter. Trois exemples. On ne peut pas déclarer un sinistre sur un contrat résilié ou expiré : ce serait une donnée sans aucun sens juridique. La date de survenance doit tomber dans la période de couverture. Et on ne peut pas supprimer un client qui a encore des contrats. Ces contrôles sont placés dans la couche service, donc ils s'appliquent quel que soit le point d'entrée. Au total, dix règles sont implémentées.

**[D9 — Documentation]**
L'API est auto-documentée avec OpenAPI. J'ai configuré le schéma de sécurité pour que le bouton « Authorize » de Swagger permette de coller son jeton et de tester ensuite toutes les routes protégées depuis le navigateur. Pendant le développement, cela a été mon outil de test principal.

**[D10 — Docker]**
Pour la portabilité, j'ai décrit l'application et sa base en deux services Docker Compose. Le Dockerfile est en deux étapes : la première compile avec Maven, la seconde ne garde que l'archive exécutable dans une image contenant uniquement un environnement d'exécution Java. L'image finale ne contient donc ni le code source ni Maven. J'ai ajouté un contrôle de santé sur PostgreSQL, pour que l'API ne démarre pas avant que la base accepte réellement les connexions. Je précise le niveau : c'est un environnement de développement reproductible, ce n'est pas une configuration de production.

**[D11 — Frontend]**
En fin de stage, j'ai développé une interface Angular. L'objectif était double : découvrir un framework front-end, et surtout vérifier concrètement que mon API était consommable par un vrai client navigateur — ce qui met à l'épreuve la configuration CORS et la transmission du jeton. J'ai réalisé quatre écrans : connexion, tableau de bord, liste et création de clients, avec un intercepteur HTTP qui ajoute automatiquement le jeton et une garde de route. Ce qui n'existe pas : les écrans contrats et sinistres, la gestion d'un jeton expiré, les fichiers d'environnement. C'est une maquette d'apprentissage, pas un livrable.

**[D12 — Critique et bilan]**
Je termine par un bilan honnête. Trois limites. La plus importante : mes annotations d'autorisation par rôle sont présentes dans le code mais inopérantes, parce que je n'ai pas ajouté l'annotation d'activation ; concrètement, tout utilisateur authentifié peut supprimer un client. C'est plus dangereux qu'une absence de protection, parce que ça donne une fausse impression de sécurité. Deuxième limite : mon gestionnaire d'erreurs renvoie systématiquement un code 400, y compris quand une ressource n'existe pas, où il faudrait un 404. Troisième limite : ma couverture de tests est très faible, un seul test unitaire.
Ce que j'ai gagné : la compréhension du rôle du serveur comme garant des règles métier, la notion de contrat d'interface, et l'intérêt d'un environnement reproductible.
Les priorités si je poursuivais : activer réellement l'autorisation, différencier les codes d'erreur, écrire les tests des contrôleurs, et mettre en place une intégration continue.
Je vous remercie de votre attention et je suis à votre disposition pour vos questions.

---

## 3. Questions probables et réponses courtes

**Q1 — Pourquoi avoir choisi JWT plutôt qu'une session HTTP classique ?**
Parce que l'API doit rester sans état. Avec une session, le serveur stocke l'état de chaque utilisateur connecté, ce qui complique la réplication et couple le client au serveur. Avec un jeton signé, chaque requête se suffit à elle-même : le serveur vérifie la signature et l'expiration, sans consulter de stockage de session. C'est aussi ce qui rend l'API consommable par des clients variés — navigateur, Postman, application mobile. La contrepartie que j'assume : un jeton émis ne peut pas être révoqué avant son expiration.

**Q2 — Vous avez désactivé la protection CSRF. N'est-ce pas une faille ?**
Pas dans ce contexte. Une attaque CSRF exploite le fait que le navigateur envoie automatiquement les cookies vers un domaine donné. Ici, l'authentification passe par un en-tête `Authorization` que le navigateur n'ajoute jamais tout seul : un site tiers ne peut donc pas forger une requête authentifiée. En revanche, si je passais à une authentification par cookie, il faudrait immédiatement réactiver la protection CSRF. C'est un choix contextuel, pas une règle générale.

**Q3 — Que se passe-t-il si un jeton est volé ?**
L'attaquant peut agir jusqu'à l'expiration, soit au maximum 24 heures. C'est une vraie limite de mon implémentation, et je l'ai identifiée. Les corrections seraient : réduire la durée de vie à quelques minutes, ajouter un jeton de rafraîchissement, et prévoir une liste de révocation. Côté client, le stockage du jeton dans le `localStorage` est également vulnérable aux injections de script ; un cookie `HttpOnly` serait plus sûr, mais imposerait alors de gérer le CSRF.

**Q4 — Votre Docker est-il prêt pour la production ?**
Non, et je ne le présente pas comme tel. Ce que j'ai fait, c'est un environnement de développement reproductible : deux commandes suffisent à lancer l'API et sa base sur un poste vierge. Ce qui manque pour la production : l'application tourne en `root` dans le conteneur, les identifiants sont en clair dans le fichier de composition, il n'y a pas de limite de ressources, pas de profil de production, pas de gestion de secrets, et les tests sont ignorés au moment de la construction de l'image. Je saurais dire quoi corriger, mais je ne l'ai pas fait.

**Q5 — Comment avez-vous organisé votre travail sous Git ?**
J'ai fait vingt-neuf commits sur huit journées de développement, en suivant la convention Conventional Commits : chaque message est préfixé par le type de changement, ce qui rend l'historique lisible. Je reconnais deux faiblesses de méthode. D'abord, j'ai tout committé sur une seule branche, alors que le client Angular, qui sortait du périmètre demandé, aurait dû aller sur une branche de fonctionnalité dédiée pour ne pas risquer de déstabiliser un backend déjà validé. Ensuite, mon commit du 21 juillet mélange deux sujets sans rapport : l'ajout des fichiers Docker et le début du service d'authentification Angular. Un commit devrait porter un seul changement cohérent. C'est le point de méthode que je retiens le plus de ce stage.

**Q6 — Votre architecture en trois couches n'est-elle pas surdimensionnée pour un projet de cette taille ?**
Elle a un coût réel : plus de classes, et des mappers écrits à la main. Mais elle m'a apporté deux choses concrètes. D'abord, j'ai pu écrire un test unitaire du service de sinistres avec des repositories simulés, sans démarrer Spring ni base de données — impossible si la logique était dans le contrôleur. Ensuite, les règles métier sont à un seul endroit, donc elles s'appliquent quel que soit le point d'entrée. Sur un projet plus petit encore, je reconnais que ce serait discutable.

**Q7 — Pourquoi vos annotations `@PreAuthorize` ne fonctionnent-elles pas ?**
Parce que Spring Security n'active pas la sécurité au niveau des méthodes par défaut : il faut ajouter `@EnableMethodSecurity` sur une classe de configuration. Je ne l'ai pas fait, donc les annotations sont ignorées à l'exécution. Je l'ai découvert en relisant mon code pour rédiger le rapport, et non par un test — ce qui illustre exactement pourquoi les tests d'autorisation sont nécessaires : sans test, une protection absente ressemble à une protection présente. La correction est d'une ligne, plus les tests qui vérifient effectivement les refus.

**Q8 — Comment testez-vous votre application ?**
Honnêtement, insuffisamment. J'ai deux tests automatisés : un test de démarrage du contexte Spring, et un test unitaire Mockito qui vérifie qu'un sinistre est refusé sur un contrat résilié. Le reste a été vérifié manuellement via Swagger UI et en inspectant la base avec `psql`. Ce qui manque : des tests de contrôleur avec `@WebMvcTest`, des tests d'intégration de la chaîne de sécurité, et une mesure de couverture. Idéalement, Testcontainers permettrait de tester sur une vraie base PostgreSQL jetable, sans dépendre de l'environnement local.

**Q9 — Pourquoi `ddl-auto: update` et non un outil de migration ?**
Parce que c'était le plus rapide pour itérer sur le modèle pendant le développement : Hibernate ajuste le schéma automatiquement. Mais c'est inadapté dès qu'il y a des données à préserver : ce mode n'exécute aucune suppression ni renommage, ne trace pas les évolutions et ne permet pas de revenir en arrière. Flyway serait la bonne réponse : chaque évolution du schéma devient un script versionné, rejouable et traçable.

**Q10 — Quelle a été votre plus grosse difficulté technique ?**
L'exception de chargement paresseux au moment de sérialiser les réponses. Mes associations sont en `FetchType.LAZY` et j'ai désactivé `open-in-view`, donc la session Hibernate est fermée quand la réponse est construite : accéder au client d'un contrat à ce moment-là lève une exception. La solution a été de convertir en DTO à l'intérieur de la méthode transactionnelle. Ce qui m'a le plus appris, c'est de comprendre que `open-in-view` ne causait pas le problème : il le rendait visible au lieu de le masquer derrière des requêtes SQL émises silencieusement.

**Q11 — Quel est l'intérêt des DTO ? Pourquoi ne pas renvoyer directement les entités ?**
Trois raisons. Le contrôle de ce qui est exposé : l'entité `User` contient le mot de passe haché, que je ne veux jamais sérialiser. La différenciation des contrats d'entrée : mon DTO de mise à jour de client ne contient pas le CIN, donc il est techniquement impossible de modifier un identifiant national via cette route. Et le découplage : je peux faire évoluer le modèle persistant sans casser les clients de l'API, tant que je maintiens le DTO.

**Q12 — Si vous aviez une semaine de plus, que feriez-vous ?**
Dans cet ordre : activer réellement l'autorisation par rôle avec les tests qui la vérifient ; différencier les codes d'erreur HTTP, notamment le 404 ; externaliser le secret JWT ; écrire les tests des contrôleurs ; et mettre en place une intégration continue qui compile et exécute les tests à chaque poussée. Je ne toucherais pas au frontend : le périmètre demandé passe avant.

---

## 4. Conseils de présentation

- **Répéter avec un chronomètre.** L'objectif est 9 minutes ; au-delà de 10, le jury coupe.
- **Préparer la démonstration en secours** : si une démo live est prévue, avoir des captures d'écran de rechange (le réseau, Docker ou la base peuvent faire défaut).
- **Ne pas lire les diapositives.** Elles servent d'appui visuel ; le contenu est dans le discours.
- **Assumer les limites sans se dévaloriser.** Formuler « j'ai identifié tel défaut, voici la correction » et non « je n'ai pas réussi à ».
- **Ne jamais afficher de secret réel** ; les comptes de démonstration doivent être annoncés comme tels.
- **Vocabulaire métier** : dire « sinistre », « police », « prime », « couverture » plutôt que « ticket » ou « objet ». Le jury et l'encadrante sont du domaine assurance.
- **Prévoir des diapositives de secours** (non numérotées, après la conclusion) : extrait du filtre JWT, contenu du `docker-compose.yml`, tableau complet des endpoints. Elles servent uniquement à répondre à une question précise.
