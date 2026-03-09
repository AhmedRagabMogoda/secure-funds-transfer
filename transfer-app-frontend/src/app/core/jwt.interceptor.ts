import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { ApiResponse } from '../models/api-response.model';
import { AuthResponse } from '../models/auth-response.model';

/**
 * HTTP Interceptor that handles JWT attachment and automatic token refresh.
 *
 * On every outgoing request:
 *   1. Attaches the stored access token as a Bearer header
 *
 * On a 401 Unauthorized response:
 *   2. Attempts to refresh the access token using the stored refresh token
 *   3. Retries the original failed request with the new access token
 *   4. If the refresh itself fails, logs the user out
 *
 * Concurrent 401 requests are queued until the refresh completes,
 * preventing multiple simultaneous refresh calls (the "refresh race condition").
 */
@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  private isRefreshing = false;
  private refreshTokenSubject = new BehaviorSubject<string | null>(null);

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // Skip token attachment for auth endpoints
    if (this.isAuthEndpoint(request.url)) {
      return next.handle(request);
    }

    const token = this.authService.getAccessToken();
    if (token) {
      request = this.attachToken(request, token);
    }

    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return this.handle401(request, next);
        }
        return throwError(() => error);
      })
    );
  }

  // ---------------------------------------------------------------------------
  // Private helpers
  // ---------------------------------------------------------------------------

  private handle401(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    if (!this.isRefreshing) {
      // Begin refresh — block other requests until complete
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((response: ApiResponse<AuthResponse>) => {
          this.isRefreshing = false;
          const newToken = response.data.accessToken;
          this.refreshTokenSubject.next(newToken);
          return next.handle(this.attachToken(request, newToken));
        }),
        catchError(error => {
          // Refresh token itself is expired or invalid — force logout
          this.isRefreshing = false;
          this.authService.logout();
          return throwError(() => error);
        })
      );
    }

    // A refresh is already in progress — wait for it to complete, then retry
    return this.refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap(token => next.handle(this.attachToken(request, token!)))
    );
  }

  private attachToken(request: HttpRequest<unknown>, token: string): HttpRequest<unknown> {
    return request.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  private isAuthEndpoint(url: string): boolean {
    return url.includes('/api/auth/login') || url.includes('/api/auth/refresh');
  }
}
