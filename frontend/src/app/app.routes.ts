import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { ClientListComponent } from './features/client/client-list/client-list.component';
import { ClientFormComponent } from './features/client/client-form/client-form.component';
import { ContractListComponent } from './features/contract/contract-list/contract-list';
import { ClaimListComponent } from './features/claim/claim-list/claim-list';
import { authGuard } from './core/guards/auth.guard';
import { ClaimFormComponent } from './features/claim/claim-form/claim-form';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'clients', component: ClientListComponent, canActivate: [authGuard] },
  { path: 'clients/new', component: ClientFormComponent, canActivate: [authGuard] },
  { path: 'contracts', component: ContractListComponent, canActivate: [authGuard] },
  { path: 'contracts/:id', loadComponent: () => import('./features/contract/contract-detail/contract-detail').then(m => m.ContractDetailComponent), canActivate: [authGuard] },
  { path: 'claims', component: ClaimListComponent, canActivate: [authGuard] },
  { path: 'claims/new', component: ClaimFormComponent, canActivate: [authGuard] },
  { path: 'claims/:id', loadComponent: () => import('./features/claim/claim-detail/claim-detail').then(m => m.ClaimDetailComponent), canActivate: [authGuard] },
  { path: 'clients/:id', loadComponent: () => import('./features/client/client-detail/client-detail').then(m => m.ClientDetailComponent), canActivate: [authGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },  // <-- TOUJOURS EN DERNIER
];
