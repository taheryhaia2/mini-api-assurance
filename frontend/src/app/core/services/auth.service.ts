import { Injectable, signal } from '@angular/core'; // Ajoute signal
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponse, LoginRequest, Role } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'jwt_token';
  private readonly USERNAME_KEY = 'username';
  private readonly ROLE_KEY = 'role';

  // Signal réactif pour le rôle
  readonly roleSignal = signal<Role | null>(null);

  constructor(private http: HttpClient) {
    // Au démarrage, on initialise le signal avec ce qu'il y a dans le localStorage
    const savedRole = localStorage.getItem(this.ROLE_KEY) as Role | null;
    this.roleSignal.set(savedRole);
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.API}/login`, credentials)
      .pipe(
        tap((res) => {
          localStorage.setItem(this.TOKEN_KEY, res.token);
          localStorage.setItem(this.USERNAME_KEY, res.username);
          localStorage.setItem(this.ROLE_KEY, res.role);
          this.roleSignal.set(res.role); // On met à jour le signal !
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    this.roleSignal.set(null); // On vide le signal
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getUsername(): string | null {
    return localStorage.getItem(this.USERNAME_KEY);
  }

  getRole(): Role | null {
    return this.roleSignal(); // On lit depuis le signal
  }

  isAdmin(): boolean {
    return this.roleSignal() === 'ADMIN';
  }
}
