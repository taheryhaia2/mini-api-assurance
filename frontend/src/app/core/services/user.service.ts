import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserRegisterRequest } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly API_USERS = 'http://localhost:8080/api/users';
  private readonly API_AUTH = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.API_USERS);
  }

  registerUser(data: UserRegisterRequest): Observable<any> {
    return this.http.post(`${this.API_AUTH}/register`, data);
  }
}
