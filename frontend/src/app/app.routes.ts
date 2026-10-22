import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { ClientListComponent } from './features/client/client-list/client-list.component';
import { ClientFormComponent } from './features/client/client-form/client-form.component';
import { ContractListComponent } from './features/contract/contract-list/contract-list';
import { ContractFormComponent } from './features/contract/contract-form/contract-form';
import { ClaimListComponent } from './features/claim/claim-list/claim-list';
import { ClaimFormComponent } from './features/claim/claim-form/claim-form';
import { UserListComponent } from './features/users/user-list/user-list';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },

  // Clients
  { path: 'clients', component: ClientListComponent, canActivate: [authGuard] },
  { path: 'clients/new', component: ClientFormComponent, canActivate: [authGuard] },
  { path: 'clients/:id', loadComponent: () => import('./features/client/client-detail/client-detail').then(m => m.ClientDetailComponent), canActivate: [authGuard] },

  // Contrats (ordre strict : liste, puis new, puis :id)
  { path: 'contracts', component: ContractListComponent, canActivate: [authGuard] },
  { path: 'contracts/new', component: ContractFormComponent, canActivate: [authGuard] },
  { path: 'contracts/:id', loadComponent: () => import('./features/contract/contract-detail/contract-detail').then(m => m.ContractDetailComponent), canActivate: [authGuard] },

  // Sinistres
  { path: 'claims', component: ClaimListComponent, canActivate: [authGuard] },
  { path: 'claims/new', component: ClaimFormComponent, canActivate: [authGuard] },
  { path: 'claims/:id', loadComponent: () => import('./features/claim/claim-detail/claim-detail').then(m => m.ClaimDetailComponent), canActivate: [authGuard] },

  // Admin
  { path: 'users', component: UserListComponent, canActivate: [authGuard, roleGuard] },

  // Redirections
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];
