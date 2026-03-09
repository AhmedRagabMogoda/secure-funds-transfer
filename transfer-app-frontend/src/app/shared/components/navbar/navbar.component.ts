import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../../core/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  role = '';
  currentUrl = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.role = this.authService.getRole() || '';
    this.currentUrl = this.router.url;
    this.router.events.pipe(filter(e => e instanceof NavigationEnd))
      .subscribe((e: any) => this.currentUrl = e.urlAfterRedirects);
  }

  logout(): void { this.authService.logout(); }
  go(path: string): void { this.router.navigate([path]); }
  isActive(path: string): boolean { return this.currentUrl.startsWith(path); }
}
