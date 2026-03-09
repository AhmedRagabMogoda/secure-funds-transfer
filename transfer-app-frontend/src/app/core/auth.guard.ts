import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';

/**
 * Route guard that protects private routes from unauthenticated access.
 *
 * Applied to /dashboard and /transfer in the routing module.
 *
 * If the user holds a valid, non-expired access token → navigation proceeds.
 * Otherwise → the user is redirected to /login.
 *
 * Note: token expiry is checked client-side by decoding the JWT payload.
 * The interceptor handles the case where the token expires mid-session.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean | UrlTree {
    if (this.authService.isAuthenticated()) {
      return true;
    }
    return this.router.createUrlTree(['/login']);
  }
}
