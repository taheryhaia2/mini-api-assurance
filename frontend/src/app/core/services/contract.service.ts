import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type ContractType = 'AUTO' | 'HOME' | 'HEALTH' | 'LIFE';

export interface ContractCreateDto {
  clientId: number;
  type: ContractType;
  startDate: string;
  endDate: string;
  coverageAmount: number;
  premiumAmount: number;
}

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
  private apiUrl = 'http://localhost:8080/api/contracts';

  getContracts(): Observable<Contract[]> {
    return this.http.get<Contract[]>(this.apiUrl);
  }

  getContractById(id: number): Observable<Contract> {
    return this.http.get<Contract>(`${this.apiUrl}/${id}`);
  }

  getContractsByClient(clientId: number): Observable<Contract[]> {
    return this.http.get<Contract[]>(`${this.apiUrl}/client/${clientId}`);
  }

  createContract(dto: ContractCreateDto): Observable<Contract> {
    return this.http.post<Contract>(this.apiUrl, dto);
  }
}
