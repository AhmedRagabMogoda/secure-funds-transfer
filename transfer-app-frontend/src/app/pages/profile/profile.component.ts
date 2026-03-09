import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { UserProfileResponse } from '../../models/user-profile.model';
import { fadeSlideIn, cardEnter } from '../../core/animations';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  animations: [fadeSlideIn, cardEnter]
})
export class ProfileComponent implements OnInit {

  profile: UserProfileResponse | null = null;
  isLoading = true;
  error = '';

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.userService.getProfile().subscribe({
      next: res => { this.profile = res.data; this.isLoading = false; },
      error: () => { this.error = 'Failed to load profile.'; this.isLoading = false; }
    });
  }

  goToTransfer(): void { this.router.navigate(['/transfer']); }
  goToHistory(): void  { this.router.navigate(['/history']); }

  getInitials(username: string): string {
    return username ? username.charAt(0).toUpperCase() : '?';
  }

  getRoleBadgeClass(role: string): string {
    return role === 'ADMIN' ? 'admin' : 'user';
  }

  get netFlow(): number {
    if (!this.profile) return 0;
    return this.profile.totalReceived - this.profile.totalSent;
  }
}
