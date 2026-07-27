# Notes de mise en page Word — checklist ISIMG

Document d'accompagnement pour convertir `RAPPORT_DE_STAGE.md` en document Word conforme aux consignes de l'ISIMG.

---

## 1. Paramètres du document

| Paramètre | Valeur exigée | Où le régler dans Word |
|---|---|---|
| Police du corps | Times New Roman, 12 pts | Accueil → Police |
| Interligne | 1,5 | Accueil → Paragraphe → Interligne |
| Alignement | Justifié | Accueil → Paragraphe → Justifier (`Ctrl + J`) |
| Espacement avant/après paragraphe | 0 pt avant, 6 pt après | Accueil → Paragraphe |
| Retrait de première ligne | 1 cm (optionnel, à garder constant) | Accueil → Paragraphe → Retrait |
| Marge haute | 2,5 cm | Mise en page → Marges → Marges personnalisées |
| Marge basse | 2,5 cm | idem |
| Marge gauche | 3 cm | idem |
| Marge droite | 2,5 cm | idem |
| En-tête (distance au bord) | 1,0 cm | Mise en page → Marges → onglet Disposition |
| Pied de page (distance au bord) | 1,0 cm | idem |
| Langue de vérification | Français (Tunisie ou France) | Révision → Langue |
| Coupure de mots | Activée (améliore la justification) | Mise en page → Coupure de mots → Automatique |

**Interdits de forme**
- Aucun soulignement pour l'emphase : uniquement **gras** ou *italique*.
- Aucun titre entièrement en majuscules.
- Aucune couleur de police autre que le noir dans le corps du texte.
- Pas d'emoji ni d'icône décorative.

---

## 2. Styles de titres

Utiliser **impérativement** les styles Word (et non un formatage manuel), sans quoi la table des matières automatique ne fonctionnera pas.

| Niveau | Style Word | Format | Numérotation |
|---|---|---|---|
| Chapitre | Titre 1 | Times New Roman 16 pts, gras, noir, saut de page avant | 1., 2., 3. |
| Section | Titre 2 | Times New Roman 14 pts, gras, noir | 1.1., 1.2. |
| Sous-section | Titre 3 | Times New Roman 12 pts, gras italique, noir | 1.1.1. |

**Numérotation décimale automatique** : Accueil → Liste à plusieurs niveaux → « Définir une nouvelle liste à plusieurs niveaux » → lier le niveau 1 au style Titre 1, le niveau 2 au style Titre 2, le niveau 3 au style Titre 3. Format `1.` / `1.1.` / `1.1.1.`.

> Les styles Titre 1/2/3 sont noirs par défaut en bleu dans Word : modifier le style (clic droit → Modifier) pour repasser en noir et en Times New Roman.

Les sections liminaires (Remerciements, Table des matières, Liste des figures, Liste des tableaux, Introduction, Conclusion, Bibliographie, Annexes) prennent le style **Titre 1 sans numéro** : les inclure dans la table des matières mais les exclure de la numérotation décimale (clic droit sur le paragraphe → « Ne pas numéroter »).

---

## 3. Pagination et sections

Découper le document en trois sections Word (Mise en page → Sauts de page → Saut de section « Page suivante ») :

| Section | Contenu | Numérotation |
|---|---|---|
| 1 | Page de garde | Aucun numéro |
| 2 | Remerciements, table des matières, listes | Chiffres romains minuscules (i, ii, iii) — *facultatif* |
| 3 | Introduction → Annexes | Chiffres arabes, **recommencer à 1** |

Pour recommencer la numérotation : double-clic dans le pied de page → décocher « Lier au précédent » → Insertion → Numéro de page → Format → « À partir de : 1 ».

**En-tête** (à partir de la section 3, désactiver « Lier au précédent ») :
- Aligné à gauche : `Mini-API Assurance — Vermeg` (33 caractères, sous la limite de 40)
- Aligné à droite : `Taher Yahia`
- Police Times New Roman 10 pts, filet horizontal fin en dessous

**Pied de page** : numéro de page centré, Times New Roman 10 pts.

Chaque chapitre commence sur une nouvelle page : cocher « Saut de page avant » dans le style Titre 1 (Modifier le style → Format → Paragraphe → Enchaînements).

---

## 4. Table des matières et listes automatiques

1. **Table des matières** : placer le curseur après les remerciements → Références → Table des matières → « Table automatique » → puis « Table des matières personnalisée » pour limiter à 3 niveaux. Supprimer ensuite la liste manuelle présente dans le fichier Markdown.
2. **Liste des figures** : Références → Insérer une table des illustrations → Légende : « Figure ».
3. **Liste des tableaux** : Références → Insérer une table des illustrations → Légende : « Tableau ».
4. **Mise à jour finale obligatoire** : sélectionner tout le document (`Ctrl + A`) puis `F9` → « Mettre à jour toute la table ». À refaire juste avant l'export PDF.

---

## 5. Figures

- Les cinq figures du rapport sont fournies en syntaxe **Mermaid**. Deux méthodes pour les intégrer :
  - **Recommandée** : coller le code Mermaid dans https://mermaid.live, exporter en **PNG à 2x ou en SVG**, puis insérer l'image dans Word.
  - **Alternative** : redessiner le schéma avec draw.io / diagrams.net pour un rendu plus personnalisable.
- Insérer l'image « Aligné sur le texte », centrée, largeur maximale 15 cm.
- **Légende sous la figure** : clic droit sur l'image → Insérer une légende → Étiquette « Figure » → position « Sous l'élément sélectionné ». Format : `Figure 1 — Architecture globale de la solution`, Times New Roman 10 pts, italique, centré.
- Numérotation automatique par le champ de légende (ne jamais taper le numéro à la main).
- **Chaque figure doit être citée dans le texte** avant son apparition (« …comme l'illustre la figure 1 »). Les cinq figures le sont déjà dans le rapport fourni ; vérifier après toute coupe.

Correspondance figure ↔ section :

| Figure | Section | Citée dans le texte |
|---|---|---|
| Figure 1 — Architecture globale | 3.1 | Oui |
| Figure 2 — Modèle de domaine | 3.2 | Oui |
| Figure 3 — Séquence d'authentification JWT | 3.4 | Oui |
| Figure 4 — Composition Docker Compose | 3.6 | Oui (et rappelée en 4.7) |
| Figure 5 — Chaîne d'appel du frontend | 4.8 | Oui |

---

## 6. Tableaux

- **Légende au-dessus du tableau** : clic dans le tableau → Références → Insérer une légende → Étiquette « Tableau » → position « Au-dessus de l'élément sélectionné ».
- Format de légende : `Tableau 1 — Besoins fonctionnels retenus`, Times New Roman 10 pts, italique, centré.
- Corps du tableau : Times New Roman 10 ou 11 pts, interligne simple (dérogation admise à l'interligne 1,5 dans les tableaux), alignement à gauche pour le texte, centré pour les valeurs courtes.
- Ligne d'en-tête en gras, avec « Répéter les lignes d'en-tête » activé si le tableau se poursuit sur deux pages (Disposition → Répéter les lignes d'en-têtes).
- Style de bordure sobre : « Tableau simple » ou « Grille du tableau », sans trame de couleur vive.
- **Chaque tableau doit être cité dans le texte.** Vérifier les sept tableaux.

---

## 7. Extraits de code

Le rapport contient trois extraits de code courts, volontairement limités.

- Police **Consolas ou Courier New, 9 ou 10 pts**, interligne simple, non justifié.
- Encadrer d'une bordure fine ou d'une trame gris très clair.
- Ne pas dépasser une quinzaine de lignes par extrait.
- Faire suivre chaque extrait d'une phrase explicative en italique 10 pts (déjà rédigée dans le rapport sous forme de légende « Extrait n — … »).
- Si le jury exige que les extraits soient traités comme des figures, les légender avec l'étiquette « Figure » et les ajouter à la liste des figures.

---

## 8. Typographie française

| Signe | Règle | Raccourci Word |
|---|---|---|
| `;` `:` `?` `!` `%` | Espace **insécable** avant | `Ctrl + Maj + Espace` |
| `«` `»` | Espace insécable à l'intérieur des guillemets | idem |
| `,` `.` | Pas d'espace avant, une espace après | — |
| Tiret de dialogue / incise | Tiret cadratin `—` | `Alt + 0151` |
| Sigles | Sans points (ISIMG, API, JWT), développés à la première occurrence | — |

Activer la correction automatique française : Fichier → Options → Vérification → Options de correction automatique → onglet « Mise en forme automatique au cours de la frappe » → cocher les guillemets français.

Vérification finale : Révision → Grammaire et orthographe, puis relecture manuelle (le correcteur ne détecte pas les accords de participes ni les répétitions).

---

## 9. Bibliographie

- Placer la bibliographie après la conclusion, avant les annexes.
- Numérotation entre crochets `[1]`, `[2]`… dans l'ordre de première citation dans le texte.
- Format cohérent pour toutes les entrées : Auteur ou organisme, « Titre », support/éditeur, année. [En ligne]. Disponible : URL — consulté le JJ mois AAAA.
- **Règle de cohérence stricte** : toute référence citée dans le texte doit figurer en bibliographie, et toute référence en bibliographie doit être citée dans le texte. Le rapport signale ce point pour les références [9] à [14] : soit les citer dans le chapitre 4, soit les retirer.
- Ne pas laisser les URL en bleu souligné : sélectionner puis « Supprimer le lien hypertexte », ou appliquer le style Normal.

---

## 10. Page de garde

Reprendre le **modèle officiel téléchargeable depuis le compte ISIMG**. Éléments minimaux à faire figurer :

- République Tunisienne — Ministère de l'Enseignement Supérieur et de la Recherche Scientifique
- Université de Gabès — Institut Supérieur d'Informatique et de Multimédia de Gabès
- Logos institutionnels
- Mention « Rapport de stage d'initiation » et niveau de formation : cycle d'ingénieur Génie Logiciel (FIGL1)
- Titre du travail
- Nom et prénom de l'étudiant : Taher Yahia
- Organisme d'accueil : Vermeg
- Encadrante : Mme Faten Kardous, Lead Developer, Insurance Market Operations
- Période : 1er — 31 juillet 2026
- Année universitaire : 2025 / 2026
- Contact étudiant : taher.yahia@isimg.tn — 58 780 980

La page de garde ne porte ni en-tête, ni pied de page, ni numéro de page.

---

## 11. Checklist finale avant impression

- [ ] Marges 2,5 / 2,5 / 3 / 2,5 cm appliquées à tout le document
- [ ] Times New Roman 12, interligne 1,5, texte justifié partout dans le corps
- [ ] Aucun texte souligné, aucun titre en majuscules
- [ ] Styles Titre 1/2/3 utilisés partout, numérotation décimale correcte et continue
- [ ] Table des matières générée automatiquement et **mise à jour**
- [ ] Liste des figures et liste des tableaux générées et mises à jour
- [ ] 5 figures numérotées, légendées **en dessous**, toutes citées dans le texte
- [ ] 7 tableaux numérotés, légendés **au-dessus**, tous cités dans le texte
- [ ] En-tête présent à partir de l'introduction (titre ≤ 40 caractères + nom)
- [ ] Numéros de page en pied de page, page de garde non numérotée
- [ ] Chaque chapitre débute sur une nouvelle page
- [ ] Bibliographie cohérente dans les deux sens (texte ↔ liste)
- [ ] Espaces insécables devant `;` `:` `?` `!` `%`
- [ ] Le seul `[À COMPLÉTER]` restant (entité et adresse exactes de Vermeg) est renseigné ou supprimé
- [ ] Blocs « notes de mise en page » et « note de volumétrie » **supprimés** du document final
- [ ] Volume du corps ≈ 15 pages maximum hors liminaires et annexes
- [ ] Correction orthographique et grammaticale effectuée
- [ ] Aucun secret réel dans le document (seuls des comptes de démonstration sont mentionnés, et présentés comme tels)
- [ ] Export PDF final vérifié page par page (positions des figures, tableaux non coupés)
