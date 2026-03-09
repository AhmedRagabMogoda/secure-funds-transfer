import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TransferService } from '../../services/transfer.service';
import { UserService } from '../../services/user.service';
import { AccountResponse } from '../../models/account.model';
import { TransactionResponse } from '../../models/transfer.model';
import { UserProfileResponse } from '../../models/user-profile.model';
import { fadeSlideIn, listStagger, cardEnter } from '../../core/animations';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  animations: [fadeSlideIn, listStagger, cardEnter]
})
export class DashboardComponent implements OnInit {

  account: AccountResponse | null = null;
  profile: UserProfileResponse | null = null;
  recentTransactions: TransactionResponse[] = [];

  isLoadingAccount  = true;
  isLoadingHistory  = true;
  isLoadingProfile  = true;

  displayedColumns = ['date', 'direction', 'counterparty', 'amount', 'status'];

  constructor(
    private accountService: AccountService,
    private transferService: TransferService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAccount();
    this.loadRecentHistory();
    this.loadProfile();
  }

  loadAccount(): void {
    this.accountService.getMyAccount().subscribe({
      next: res => { this.account = res.data; this.isLoadingAccount = false; },
      error: () => { this.isLoadingAccount = false; }
    });
  }

  loadRecentHistory(): void {
    this.transferService.getHistory(0, 5).subscribe({
      next: res => { this.recentTransactions = res.data.content; this.isLoadingHistory = false; },
      error: () => { this.isLoadingHistory = false; }
    });
  }

  loadProfile(): void {
    this.userService.getProfile().subscribe({
      next: res => { this.profile = res.data; this.isLoadingProfile = false; },
      error: () => { this.isLoadingProfile = false; }
    });
  }

  counterparty(tx: TransactionResponse): string {
    return tx.direction === 'SENT' ? tx.receiverAccountNumber : tx.senderAccountNumber;
  }
  goTransfer(): void  { this.router.navigate(['/transfer']); }
  goHistory(): void   { this.router.navigate(['/history']); }
  goProfile(): void   { this.router.navigate(['/profile']); }
  getInitials(u: string): string { return u ? u.charAt(0).toUpperCase() : '?'; }
}
