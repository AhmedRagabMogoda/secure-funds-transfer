import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TransferService } from '../../services/transfer.service';
import { AccountService } from '../../services/account.service';
import { AccountResponse } from '../../models/account.model';
import { TransferResponse } from '../../models/transfer.model';

/**
 * Transfer page component.
 *
 * Presents a reactive form for submitting a fund transfer.
 * Displays the user's current balance and account number for reference.
 * On successful transfer, shows a confirmation summary with the new balance.
 */
@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss']
})
export class TransferComponent implements OnInit {

  transferForm!: FormGroup;
  account: AccountResponse | null = null;
  transferResult: TransferResponse | null = null;

  isLoadingAccount = true;
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private transferService: TransferService,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.transferForm = this.fb.group({
      receiverAccountNumber: ['', [
        Validators.required,
        Validators.minLength(3)
      ]],
      amount: [null, [
        Validators.required,
        Validators.min(0.01)
      ]]
    });

    this.loadAccount();
  }

  get receiverAccountNumber() { return this.transferForm.get('receiverAccountNumber'); }
  get amount() { return this.transferForm.get('amount'); }

  loadAccount(): void {
    this.accountService.getMyAccount().subscribe({
      next: (res) => {
        this.account = res.data;
        this.isLoadingAccount = false;
      },
      error: () => {
        this.isLoadingAccount = false;
      }
    });
  }

  onSubmit(): void {
    if (this.transferForm.invalid) {
      this.transferForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.transferResult = null;

    this.transferService.transfer(this.transferForm.value).subscribe({
      next: (res) => {
        this.isSubmitting = false;
        this.transferResult = res.data;
        // Update displayed balance to reflect the transfer
        if (this.account) {
          this.account.balance = res.data.newBalance;
        }
        this.transferForm.reset();
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage =
          err?.error?.message || 'Transfer failed. Please try again.';
      }
    });
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}
