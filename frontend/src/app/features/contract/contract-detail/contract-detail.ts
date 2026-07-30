import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router'; // <-- Ajout Router
import { ContractService, Contract } from '../../../core/services/contract.service';

@Component({
  selector: 'app-contract-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './contract-detail.html',
  styleUrl: './contract-detail.css'
})
export class ContractDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router); // <-- Ajout
  private contractService = inject(ContractService);

  contract = signal<Contract | null>(null);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.contractService.getContractById(id).subscribe({
      next: (data: Contract) => this.contract.set(data),
      error: (err: any) => console.error('Erreur chargement détail', err)
    });
  }

  // <-- Ajout de la méthode
  goBack() {
    this.router.navigate(['/contracts']);
  }
}
