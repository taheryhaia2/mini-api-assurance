import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ContractService, ContractCreateDto, ContractType } from '../../../core/services/contract.service';

@Component({
  selector: 'app-contract-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './contract-form.html',
  styleUrl: './contract-form.css'
})
export class ContractFormComponent {
  private contractService = inject(ContractService);
  private router = inject(Router);

  types: ContractType[] = ['AUTO', 'HOME', 'HEALTH', 'LIFE'];

  formData: ContractCreateDto = {
    clientId: 0,
    type: 'AUTO',
    startDate: '',
    endDate: '',
    coverageAmount: 0,
    premiumAmount: 0
  };

  errorMessage = '';

  onSubmit(): void {
    this.errorMessage = '';
    this.contractService.createContract(this.formData).subscribe({
      next: () => {
        this.router.navigate(['/contracts']);
      },
      error: (err) => {
        console.error('Erreur création contrat', err);
        this.errorMessage = 'Erreur lors de la création du contrat. Vérifiez les champs.';
      }
    });
  }
}
