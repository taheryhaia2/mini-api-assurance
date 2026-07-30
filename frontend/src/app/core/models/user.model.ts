import { Role } from './auth.model';

export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  role: Role;
}

export interface UserRegisterRequest {
  username: string;
  password: string;
  role: Role;
  firstName: string;
  lastName: string;
  email: string;
}
