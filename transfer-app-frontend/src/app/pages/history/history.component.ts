import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { TransferService } from '../../services/transfer.service';
import { TransactionResponse } from '../../models/transfer.model';
import { PagedResponse } from '../../models/paged-response.model';
import { fadeSlideIn, listStagger, cardEnter } from '../../core/animations';

type FilterType = 'ALL' | 'SENT' | 'RECEIVED';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss'],
  animations: [fadeSlideIn, listStagger, cardEnter]
})
export class HistoryComponent implements OnInit {

  activeFilter: FilterType = 'ALL';
  paged: PagedResponse<TransactionResponse> | null = null;
  isLoading = true;
  error = '';

  page = 0;
  pageSize = 10;

  displayedColumns = ['date', 'direction', 'counterparty', 'amount', 'status'];

  constructor(private transferService: TransferService) {}

  ngOnInit(): void { this.load(); }

  setFilter(f: FilterType): void {
    this.activeFilter = f;
    this.page = 0;
    this.load();
  }

  onPageChange(e: PageEvent): void {
    this.page = e.pageIndex;
    this.pageSize = e.pageSize;
    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.error = '';

    const req = this.activeFilter === 'SENT'
      ? this.transferService.getSent(this.page, this.pageSize)
      : this.activeFilter === 'RECEIVED'
        ? this.transferService.getReceived(this.page, this.pageSize)
        : this.transferService.getHistory(this.page, this.pageSize);

    req.subscribe({
      next: res => { this.paged = res.data; this.isLoading = false; },
      error: () => { this.error = 'Failed to load transactions.'; this.isLoading = false; }
    });
  }

  counterparty(tx: TransactionResponse): string {
    return tx.direction === 'SENT' ? tx.receiverAccountNumber : tx.senderAccountNumber;
  }
}
