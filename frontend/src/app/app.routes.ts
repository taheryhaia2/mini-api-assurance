import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { ClientListComponent } from './features/client/client-list/client-list.component';
import { ClientFormComponent } from './features/client/client-form/client-form.component'; // AJOUT
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'clients', component: ClientListComponent, canActivate: [authGuard] },
  { path: 'clients/new', component: ClientFormComponent, canActivate: [authGuard] }, // AJOUT
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];
