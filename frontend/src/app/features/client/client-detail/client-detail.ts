import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from '../../../core/services/client.service';
import { ContractService, Contract } from '../../../core/services/contract.service';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'app-client-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-detail.html',
  styleUrl: './client-detail.css'
})
export class ClientDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private clientService = inject(ClientService);
  private contractService = inject(ContractService);

  client = signal<Client | null>(null);
  contracts = signal<Contract[]>([]);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    // 1. On charge les infos du client
    this.clientService.getClientById(id).subscribe({
      next: (data) => this.client.set(data),
      error: (err) => console.error('Erreur client', err)
    });

    // 2. On charge les contrats de ce client
    this.contractService.getContractsByClient(id).subscribe({
      next: (data) => this.contracts.set(data),
      error: (err) => console.error('Erreur contrats', err)
    });
  }

  onViewContractDetails(contractId: number) {
    this.router.navigate(['/contracts', contractId]);
  }

  goBack() {
    this.router.navigate(['/clients']);
  }
}
