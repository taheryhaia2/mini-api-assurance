import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientService {
  private readonly API = 'http://localhost:8080/api/clients';

  constructor(private http: HttpClient) {}
    createClient(client: Client): Observable<Client> {
      return this.http.post<Client>(this.API, client);
    }
  getAllClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.API);
  }
}
