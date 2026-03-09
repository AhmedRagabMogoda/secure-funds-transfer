import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { AuthResponse } from '../models/auth-response.model';

/**
 * Core authentication service responsible for:
 * - Executing the login request
 * - Storing and retrieving tokens from localStorage
 * - Exposing helpers used by the interceptor and guard
 * - Handling logout by clearing stored tokens
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = '/api/auth';

  private readonly ACCESS_TOKEN_KEY  = 'accessToken';
  private readonly REFRESH_TOKEN_KEY = 'refreshToken';
  private readonly ROLE_KEY          = 'role';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  // ---------------------------------------------------------------------------
  // Authentication Operations
  // ---------------------------------------------------------------------------

  /**
   * Sends login credentials to the backend and stores the returned tokens.
   * The component subscribes to the returned Observable to handle
   * success navigation and error display.
   */
  login(username: string, password: string): Observable<ApiResponse<AuthResponse>> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.API_URL}/login`, { username, password })
      .pipe(
        tap(response => {
          if (response.success && response.data) {
            this.storeTokens(response.data);
          }
        })
      );
  }

  /**
   * Sends the stored refresh token to the backend and updates the access token.
   * Called automatically by the JWT interceptor on a 401 response.
   */
  refreshToken(): Observable<ApiResponse<AuthResponse>> {
    const refreshToken = this.getRefreshToken();
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.API_URL}/refresh`, { refreshToken })
      .pipe(
        tap(response => {
          if (response.success && response.data) {
            this.storeTokens(response.data);
          }
        })
      );
  }

  /**
   * Clears all stored tokens and redirects the user to the login page.
   */
  logout(): void {
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    this.router.navigate(['/login']);
  }

  // ---------------------------------------------------------------------------
  // Token Storage & Retrieval
  // ---------------------------------------------------------------------------

  private storeTokens(authResponse: AuthResponse): void {
    localStorage.setItem(this.ACCESS_TOKEN_KEY,  authResponse.accessToken);
    localStorage.setItem(this.REFRESH_TOKEN_KEY, authResponse.refreshToken);
    localStorage.setItem(this.ROLE_KEY,          authResponse.role);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.ACCESS_TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  getRole(): string | null {
    return localStorage.getItem(this.ROLE_KEY);
  }

  // ---------------------------------------------------------------------------
  // Auth State Helpers
  // ---------------------------------------------------------------------------

  /**
   * Returns true if a valid (non-expired) access token exists in storage.
   * Used by AuthGuard to protect private routes.
   */
  isAuthenticated(): boolean {
    const token = this.getAccessToken();
    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp * 1000;
      return Date.now() < expiry;
    } catch {
      return false;
    }
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }
}
