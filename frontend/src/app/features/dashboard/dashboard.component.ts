import { Component, AfterViewInit, ElementRef, ViewChild, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AuthService } from '../../core/services/auth.service';
import { ClientService } from '../../core/services/client.service';
import { ContractService } from '../../core/services/contract.service';
import { ClaimService } from '../../core/services/claim.service';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements AfterViewInit {
  @ViewChild('globalChart') chartRef!: ElementRef<HTMLCanvasElement>;

  username = '';
  totalClients = signal(0);
  totalContracts = signal(0);
  totalClaims = signal(0);

  private apiCallsReturned = 0;

  constructor(
    public authService: AuthService,
    private clientService: ClientService,
    private contractService: ContractService,
    private claimService: ClaimService
  ) {
    this.username = this.authService.getUsername() || '';
  }

  ngAfterViewInit(): void {
    this.clientService.getAllClients().subscribe({
      next: (clients) => {
        this.totalClients.set(clients.length);
        this.checkAndDrawChart();
      },
      error: (err) => console.error(err)
    });

    this.contractService.getContracts().subscribe({
      next: (contracts) => {
        this.totalContracts.set(contracts.length);
        this.checkAndDrawChart();
      },
      error: (err) => console.error(err)
    });

    this.claimService.getClaims().subscribe({
      next: (claims) => {
        this.totalClaims.set(claims.length);
        this.checkAndDrawChart();
      },
      error: (err) => console.error(err)
    });
  }

  private checkAndDrawChart(): void {
    this.apiCallsReturned++;
    if (this.apiCallsReturned === 3) {
      this.createChart();
    }
  }

  private createChart(): void {
    if (this.chartRef) {
      Chart.getChart(this.chartRef.nativeElement)?.destroy();

      new Chart(this.chartRef.nativeElement, {
        type: 'bar',
        data: {
          labels: ['Clients', 'Contrats', 'Sinistres'],
          datasets: [{
            label: 'Total',
            data: [this.totalClients(), this.totalContracts(), this.totalClaims()],
            backgroundColor: ['#0052cc', '#00875a', '#de350b'],
            borderWidth: 0
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: false }
          }
        }
      });
    }
  }
}
