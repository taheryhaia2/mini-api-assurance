import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink], // <-- AJOUT
  template: `
    <h1>Bienvenue {{ authService.getUsername() }}</h1>

    <nav>
      <!-- AJOUT DU LIEN -->
      <a routerLink="/clients" style="margin-right: 10px;">Voir les clients</a>
      <button (click)="logout()">Déconnexion</button>
    </nav>
  `,
})
export class DashboardComponent {
  constructor(public authService: AuthService, private router: Router) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
