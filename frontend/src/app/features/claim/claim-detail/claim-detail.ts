import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ClaimService, Claim } from '../../../core/services/claim.service';

@Component({
  selector: 'app-claim-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './claim-detail.html',
  styleUrl: './claim-detail.css'
})
export class ClaimDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private claimService = inject(ClaimService);

  claim = signal<Claim | null>(null);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.claimService.getClaimById(id).subscribe({
      next: (data: Claim) => this.claim.set(data),
      error: (err: any) => console.error('Erreur chargement sinistre', err)
    });
  }

  goBack() {
    this.router.navigate(['/claims']);
  }
}
