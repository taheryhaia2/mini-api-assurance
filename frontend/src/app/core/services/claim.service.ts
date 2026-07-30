import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Claim {
  id: number;
  claimNumber: string;
  contractId: number;
  description: string;
  claimDate: string;
  declarationDate: string;
  estimatedAmount: number;
  reimbursedAmount: number;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClaimService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/claims';

  getClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(this.apiUrl);
  }
  getClaimById(id: number): Observable<Claim> {
    return this.http.get<Claim>(`${this.apiUrl}/${id}`);
  }
  getClaimsByContract(contractId: number): Observable<Claim[]> {
    return this.http.get<Claim[]>(`http://localhost:8080/api/contracts/${contractId}/claims`);
  }
}
