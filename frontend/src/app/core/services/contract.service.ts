import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Contract {
  id: number;
  policyNumber: string;
  clientId: number;
  type: string;
  startDate: string;
  endDate: string;
  status: string;
  coverageAmount: number;
  premiumAmount: number;
}

@Injectable({
  providedIn: 'root'
})
export class ContractService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/contracts'; // Vérifie ton URL

  getContracts(): Observable<Contract[]> {
    return this.http.get<Contract[]>(this.apiUrl);
  }

  // C'EST CETTE METHODE QU'ON AJOUTE ICI
  getContractById(id: number): Observable<Contract> {
    return this.http.get<Contract>(`${this.apiUrl}/${id}`);
  }
}
