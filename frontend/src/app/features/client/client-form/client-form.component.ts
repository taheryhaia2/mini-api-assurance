import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Client } from '../../../core/models/client.model';
import { ClientService } from '../../../core/services/client.service';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './client-form.component.html',
  styleUrl: './client-form.component.css'
})
export class ClientFormComponent {
  client: Client = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: '',
    cin: '',
    birthDate: ''
  };

  errorMessage = signal<string | null>(null);

  constructor(private clientService: ClientService, private router: Router) {}

  onSubmit(): void {
    this.errorMessage.set(null);

    this.clientService.createClient(this.client).subscribe({
      next: () => this.router.navigate(['/clients']),
      error: (err) => {
        const body = err.error;
        const msg =
          (typeof body === 'string' ? body : body?.error) ||
          'Une erreur est survenue.';
        this.errorMessage.set(msg);
      }
    });
  }
}
