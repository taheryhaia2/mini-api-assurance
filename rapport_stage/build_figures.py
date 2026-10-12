"""Génère les figures du rapport à partir de la structure réelle du dépôt.

Les légendes ne sont pas incluses dans les images : elles sont insérées dans le
document Word sous forme de légendes numérotées (champ SEQ).

Usage : python build_figures.py   (dépend de matplotlib)
Sorties : figures/fig1_architecture.png, figures/fig2_modele_donnees.png
"""
from pathlib import Path
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch

OUT = Path(__file__).parent / "figures"
OUT.mkdir(exist_ok=True)
plt.rcParams["font.family"] = "DejaVu Sans"


def box(ax, x, y, w, h, title, lines=(), fc="#ffffff", tsize=9, lsize=7.6):
    ax.add_patch(FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.02,rounding_size=0.08",
                                fc=fc, ec="#333333", lw=1.1))
    ax.text(x + w / 2, y + h - 0.2, title, ha="center", va="top", fontsize=tsize, weight="bold")
    for i, line in enumerate(lines):
        ax.text(x + 0.12, y + h - 0.52 - i * 0.25, line, ha="left", va="top", fontsize=lsize)


def arrow(ax, x1, y1, x2, y2, label=None, lx=None, ly=None):
    ax.add_patch(FancyArrowPatch((x1, y1), (x2, y2), arrowstyle="-|>", mutation_scale=10,
                                 lw=1.0, color="#333333"))
    if label:
        ax.text(lx if lx is not None else (x1 + x2) / 2 + 0.1,
                ly if ly is not None else (y1 + y2) / 2, label, fontsize=7.2, color="#444444", va="center")


# ---------- Figure 1 : architecture en couches ----------
fig, ax = plt.subplots(figsize=(6.3, 5.2), dpi=200)
ax.set_xlim(0, 10); ax.set_ylim(0, 9.0); ax.axis("off")

box(ax, 0.3, 7.3, 4.3, 1.5, "Client HTTP", ["Swagger UI ou navigateur", "Frontend Angular (port 4200)"], fc="#eef4fb")
box(ax, 5.4, 7.3, 4.3, 1.5, "Filtre JwtAuthFilter", ["Lit l'en-tête Authorization", "Alimente le SecurityContext"], fc="#fdf3e7")
box(ax, 0.3, 5.3, 9.4, 1.6, "Couche contrôleur (REST)",
    ["AuthController · ClientController · ContractController",
     "ClaimController · UserController ; validation des DTO (Bean Validation)"], fc="#f4f4f4")
box(ax, 0.3, 3.2, 9.4, 1.6, "Couche service (règles métier, transactions)",
    ["ClientService · ContractService · ClaimService · UserService",
     "Contrôles métier ; conversion entité / DTO (mappers)"], fc="#f4f4f4")
box(ax, 0.3, 1.1, 9.4, 1.5, "Couche d'accès aux données (Spring Data JPA)",
    ["ClientRepository · ContractRepository · ClaimRepository · UserRepository"], fc="#f4f4f4")
box(ax, 0.3, 0.05, 4.3, 0.85, "PostgreSQL 16", ["Base assurance_db"], fc="#e8f5e9")
box(ax, 5.4, 0.05, 4.3, 1.05, "Stockage local uploads/", ["Justificatifs de sinistres", "(écrit par ClaimService)"], fc="#e8f5e9")

arrow(ax, 2.45, 7.3, 2.45, 6.9, "requête + jeton JWT", lx=2.55, ly=7.1)
arrow(ax, 5.4, 8.05, 4.6, 8.05)
arrow(ax, 5.4, 7.3, 5.2, 6.9)
arrow(ax, 5.0, 5.3, 5.0, 4.8, "DTO", lx=5.1, ly=5.05)
arrow(ax, 5.0, 3.2, 5.0, 2.6, "entités", lx=5.1, ly=2.9)
arrow(ax, 2.45, 1.1, 2.45, 0.9, "JDBC", lx=2.55, ly=1.0)
fig.savefig(OUT / "fig1_architecture.png", bbox_inches="tight", facecolor="white")
plt.close(fig)

# ---------- Figure 2 : modèle de données ----------
fig, ax = plt.subplots(figsize=(7.2, 5.0), dpi=200)
ax.set_xlim(0, 10); ax.set_ylim(0, 7.6); ax.axis("off")

box(ax, 0.05, 3.5, 2.85, 3.9, "Client", ["id : Long (PK)", "lastName, firstName", "email",
                                        "cin : String (unique)", "phoneNumber, address",
                                        "birthDate, createdAt", "active : boolean"], fc="#eef4fb")
box(ax, 3.45, 3.5, 3.3, 3.9, "Contract", ["id : Long (PK)", "policyNumber (unique)", "type : ContractType",
                                          "client_id : FK non nulle", "startDate, endDate",
                                          "coverageAmount : BigDecimal", "premiumAmount : BigDecimal",
                                          "status : ContractStatus"], fc="#fdf3e7")
box(ax, 6.9, 3.5, 3.05, 3.9, "Claim", ["id : Long (PK)", "claimNumber (unique)", "contract_id : FK",
                                       "description", "claimDate, declarationDate",
                                       "estimatedAmount", "reimbursedAmount",
                                       "status : ClaimStatus", "documentPath"], fc="#e8f5e9")
box(ax, 6.9, 0.3, 3.05, 3.0, "app_user (User)", ["id : Long (PK)", "username (unique)",
                                              "firstName, lastName", "email (unique)",
                                              "password (haché BCrypt)", "role : Role"], fc="#f4f4f4")
arrow(ax, 2.9, 5.45, 3.45, 5.45, lx=2.97, ly=5.85)
arrow(ax, 6.75, 5.45, 6.9, 5.45, lx=6.77, ly=5.85)
ax.text(0.05, 2.9, "Relations : Client 1..* Contract 1..* Claim", fontsize=7.4, va="top", color="#333333")
ax.text(0.05, 2.3, "User : entité d'authentification,\nsans relation avec les entités métier", fontsize=7.4,
        va="top", color="#333333")
fig.savefig(OUT / "fig2_modele_donnees.png", bbox_inches="tight", facecolor="white")
plt.close(fig)
print("figures générées :", sorted(p.name for p in OUT.glob("*.png")))
