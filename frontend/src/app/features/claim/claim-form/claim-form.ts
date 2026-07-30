import { Component, ElementRef, ViewChild, signal, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ClientService } from '../../../core/services/client.service';
import { ContractService, Contract } from '../../../core/services/contract.service';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'app-claim-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './claim-form.html',
  styleUrl: './claim-form.css'
})
export class ClaimFormComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  private http = inject(HttpClient);
  private clientService = inject(ClientService);
  private contractService = inject(ContractService);

  // Listes pour les menus déroulants
  clients = signal<Client[]>([]);
  contracts = signal<Contract[]>([]);

  // Sélections de l'utilisateur
  selectedClientId = signal<number | null>(null);
  selectedContractId = signal<number | null>(null);

  claim = {
    description: '',
    claimDate: '',
    estimatedAmount: null as number | null
  };

  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);
  fileName = signal<string | null>(null);

  ngOnInit() {
    // Au démarrage, on charge tous les clients
    this.clientService.getAllClients().subscribe({
      next: (data) => this.clients.set(data),
      error: (err) => console.error('Erreur clients', err)
    });
  }

  // Quand on choisit un client, on charge ses contrats
  onClientChange() {
    const clientId = this.selectedClientId();
    if (clientId !== null) {
      this.contractService.getContracts().subscribe({
        next: (data) => {
          // On filtre les contrats pour n'avoir que ceux du client choisi
          this.contracts.set(data.filter(c => c.clientId === clientId));
          this.selectedContractId.set(null); // On reset le contrat choisi
        },
        error: (err) => console.error('Erreur contrats', err)
      });
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.fileName.set(input.files[0].name);
    }
  }

  onSubmit() {
    this.errorMessage.set(null);
    this.successMessage.set(null);

    const contractId = this.selectedContractId();
    if (contractId === null) {
      this.errorMessage.set('Veuillez sélectionner un contrat.');
      return;
    }

    const formData = new FormData();
    formData.append('description', this.claim.description);
    formData.append('claimDate', this.claim.claimDate);
    formData.append('estimatedAmount', this.claim.estimatedAmount?.toString() || '0');

    const file = this.fileInput.nativeElement.files?.[0];
    if (file) {
      formData.append('file', file);
    }

    this.http.post(`http://localhost:8080/api/contracts/${contractId}/claims`, formData)
      .subscribe({
        next: () => {
          this.successMessage.set('Sinistre déclaré avec succès !');
          // On vide le formulaire
          this.claim = { description: '', claimDate: '', estimatedAmount: null };
          this.fileInput.nativeElement.value = '';
          this.fileName.set(null);
          this.selectedContractId.set(null);
          this.selectedClientId.set(null);
          this.contracts.set([]);
        },
        error: (err) => {
          const body = err.error;
          const msg = (typeof body === 'string' ? body : body?.error) || 'Une erreur est survenue.';
          this.errorMessage.set(msg);
        }
      });
  }
}
