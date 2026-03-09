import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { ProfileComponent } from './profile.component';
import { SharedModule } from '../../shared/shared.module';
import { AuthGuard } from '../../core/auth.guard';

const routes: Routes = [{ path: '', component: ProfileComponent, canActivate: [AuthGuard] }];

@NgModule({
  declarations: [ProfileComponent],
  imports: [
    CommonModule, RouterModule.forChild(routes), SharedModule,
    MatCardModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule, MatDividerModule
  ]
})
export class ProfileModule {}
