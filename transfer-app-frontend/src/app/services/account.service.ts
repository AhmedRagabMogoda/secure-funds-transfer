import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { AccountResponse } from '../models/account.model';

/**
 * Service for communicating with account-related backend endpoints.
 * The JWT is attached automatically by the JwtInterceptor.
 */
@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private readonly API_URL = '/api/accounts';

  constructor(private http: HttpClient) {}

  /**
   * Retrieves the authenticated user's account details.
   * Maps to GET /api/accounts/me
   */
  getMyAccount(): Observable<ApiResponse<AccountResponse>> {
    return this.http.get<ApiResponse<AccountResponse>>(`${this.API_URL}/me`);
  }
}
