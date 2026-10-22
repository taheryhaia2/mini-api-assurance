import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ContractService, Contract } from '../../../core/services/contract.service';

@Component({
  selector: 'app-contract-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './contract-list.html',
  styleUrl: './contract-list.css'
})
export class ContractListComponent implements OnInit {
  private contractService = inject(ContractService);
  private router = inject(Router);

  contracts = signal<Contract[]>([]);

  ngOnInit() {
    this.contractService.getContracts().subscribe({
      next: (data) => this.contracts.set(data),
      error: (err) => console.error('Erreur chargement contrats', err)
    });
  }

  onViewDetails(id: number) {
    this.router.navigate(['/contracts', id]);
  }
}
