import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ContractService, Contract } from '../../../core/services/contract.service';
import { ClaimService, Claim } from '../../../core/services/claim.service';

@Component({
  selector: 'app-contract-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './contract-detail.html',
  styleUrl: './contract-detail.css'
})
export class ContractDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private contractService = inject(ContractService);
  private claimService = inject(ClaimService);

  contract = signal<Contract | null>(null);
  claims = signal<Claim[]>([]);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.contractService.getContractById(id).subscribe({
      next: (data: Contract) => this.contract.set(data),
      error: (err: any) => console.error('Erreur chargement détail', err)
    });

    this.claimService.getClaimsByContract(id).subscribe({
      next: (data: Claim[]) => this.claims.set(data),
      error: (err: any) => console.error('Erreur chargement sinistres', err)
    });
  }

  goBack() {
    this.router.navigate(['/contracts']);
  }
}
