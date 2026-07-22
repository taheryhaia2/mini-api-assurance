import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- LE DICTIONNAIRE
import { Client } from '../../../core/models/client.model';
import { ClientService } from '../../../core/services/client.service';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [CommonModule], // <-- ON L'ACTIVE ICI
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.css'
})
export class ClientListComponent implements OnInit {
  clients = signal<Client[]>([]);

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.clientService.getAllClients().subscribe({
      next: (data) => {
        console.log('Données reçues :', data);
        this.clients.set(data);
      },
      error: (err) => console.error('Erreur de chargement', err)
    });
  }
}
