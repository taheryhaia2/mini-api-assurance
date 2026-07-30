import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ClaimService, Claim } from '../../../core/services/claim.service';

@Component({
  selector: 'app-claim-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './claim-list.html',
  styleUrl: './claim-list.css'
})
export class ClaimListComponent implements OnInit {
  private claimService = inject(ClaimService);
  private router = inject(Router);

  claims = signal<Claim[]>([]);

  ngOnInit() {
    this.claimService.getClaims().subscribe({
      next: (data) => this.claims.set(data),
      error: (err) => console.error('Erreur chargement sinistres', err)
    });
  }

  onViewDetails(id: number) {
    this.router.navigate(['/claims', id]);
  }
}
