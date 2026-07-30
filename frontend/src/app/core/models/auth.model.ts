export interface LoginRequest {
  username: string;
  password: string;
}

export type Role = 'ADMIN' | 'AGENT';

export interface AuthResponse {
  token: string;
  username: string;
  role: Role;
}
