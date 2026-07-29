import { Component, AfterViewInit, ElementRef, ViewChild, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AuthService } from '../../core/services/auth.service';
import { ClientService } from '../../core/services/client.service';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements AfterViewInit {
  @ViewChild('clientsChart') chartRef!: ElementRef<HTMLCanvasElement>;

  username = '';
  totalClients = signal(0);

  constructor(
    public authService: AuthService,
    private clientService: ClientService
  ) {
    this.username = this.authService.getUsername() || '';
  }

  ngAfterViewInit(): void {
    this.clientService.getAllClients().subscribe({
      next: (clients) => {
        console.log('Clients dashboard :', clients);
        this.totalClients.set(clients.length);
        this.createChart(clients.length);
      },
      error: (err) => console.error(err)
    });
  }

  private createChart(total: number): void {
    new Chart(this.chartRef.nativeElement, {
      type: 'doughnut',
      data: {
        labels: ['Clients enregistrés', 'Objectif restant'],
        datasets: [{
          data: [total, Math.max(10 - total, 0)],
          backgroundColor: ['#0052cc', '#d0d7de'],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });
  }
}
