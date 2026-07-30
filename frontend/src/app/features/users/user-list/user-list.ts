import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User, UserRegisterRequest } from '../../../core/models/user.model';
import { UserService } from '../../../core/services/user.service';
import { Role } from '../../../core/models/auth.model';
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserListComponent implements OnInit {
  users = signal<User[]>([]);
  showForm = signal(false);

  newUser: UserRegisterRequest = {
    username: '', password: '', role: 'AGENT', firstName: '', lastName: '', email: ''
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => this.users.set(data),
      error: (err) => console.error('Erreur', err)
    });
  }

  toggleForm(): void {
    this.showForm.update(v => !v);
  }

  onSubmit(): void {
    this.userService.registerUser(this.newUser).subscribe({
      next: () => {
        this.loadUsers();
        this.showForm.set(false);
        this.newUser = { username: '', password: '', role: 'AGENT', firstName: '', lastName: '', email: '' };
      },
      error: (err) => console.error('Erreur création', err)
    });
  }
}
