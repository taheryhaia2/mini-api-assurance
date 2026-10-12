# Rapport de stage technicien — Taher Yahia (VERMEG, juillet 2026)

## Contenu du dossier

| Fichier | Rôle |
|---|---|
| `Rapport_Stage_Taher_Yahia.docx` | Rapport principal (Word, styles de titres, sommaire automatique, listes de figures et de tableaux, légendes numérotées, renvois croisés). |
| `build_figures.py` | Génère les deux figures (`figures/`) à partir de la structure du dépôt. Dépend de matplotlib. |
| `figures/fig1_architecture.png`, `figures/fig2_modele_donnees.png` | Figures insérées dans le rapport. Aucune légende dans l'image : les légendes sont des champs Word. |
| `build_report.py` | Génère le DOCX avec python-docx. |
| `AUDIT_DEPOT.md` | Audit du dépôt et points à confirmer. |
| `GRILLE_AUTOEVALUATION.md` | Grille d'auto-évaluation par rapport au barème annoncé. |
| `README.md` | Ce fichier. |

Pas de PDF joint : aucun convertisseur (LibreOffice, Word) n'était disponible lors de la génération. Le PDF doit être produit depuis Word (Fichier > Exporter > PDF) après mise à jour des champs.

## Régénérer le rapport

```bash
pip install python-docx matplotlib
python build_figures.py     # régénère figures/*.png
python build_report.py      # régénère Rapport_Stage_Taher_Yahia.docx
```

Le script réécrit le DOCX entier : toute modification faite directement dans Word sera perdue. Les modifications de contenu doivent être faites dans `build_report.py`.

## Mettre à jour les champs (sommaire, listes, légendes, renvois)

- Le document est configuré pour se mettre à jour à l'ouverture (`updateFields`). Word peut demander confirmation : accepter.
- Sinon : `Ctrl+A` puis `F9` (ou clic droit sur un champ > Mettre à jour les champs).
- Le sommaire se régénère via le champ `TOC \o "1-2"`, les listes via `TOC \c "Figure"` et `TOC \c "Tableau"`.
- Les légendes utilisent `SEQ Figure` et `SEQ Tableau` ; les renvois utilisent `REF ref_<clé>` vers des signets placés sur les légendes.
- Ajouter une figure ou un tableau : insérer une légende (Insertion > Légende) ou dupliquer une légende existante, puis mettre à jour les champs. Les renvois en dur du script (`FIG_ORDER`, `TAB_ORDER`) doivent être étendus si l'ordre change ; le script vérifie la numérotation par `assert`.

## Points à vérifier avant remise

Voir la fin de `AUDIT_DEPOT.md`. En particulier : mise en page exigée par le guide (police, marges, interligne, en-tête/pied, page de garde officielle, logos), nombre de pages, nom de l'encadrante, références bibliographiques et sections à compléter marquées `[À COMPLÉTER]` ou `[À CONFIRMER]`.
