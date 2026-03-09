import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { PagedResponse } from '../models/paged-response.model';
import { TransactionResponse, TransferRequest, TransferResponse } from '../models/transfer.model';

@Injectable({ providedIn: 'root' })
export class TransferService {

  private readonly API_URL = '/api/transfers';

  constructor(private http: HttpClient) {}

  transfer(request: TransferRequest): Observable<ApiResponse<TransferResponse>> {
    return this.http.post<ApiResponse<TransferResponse>>(this.API_URL, request);
  }

  /** All transactions — paginated */
  getHistory(page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.API_URL}/history`, { params });
  }

  /** Sent only — paginated */
  getSent(page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.API_URL}/history/sent`, { params });
  }

  /** Received only — paginated */
  getReceived(page = 0, size = 10): Observable<ApiResponse<PagedResponse<TransactionResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PagedResponse<TransactionResponse>>>(`${this.API_URL}/history/received`, { params });
  }
}
