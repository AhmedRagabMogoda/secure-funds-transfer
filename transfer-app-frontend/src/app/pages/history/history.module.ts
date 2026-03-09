import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { HistoryComponent } from './history.component';
import { SharedModule } from '../../shared/shared.module';
import { AuthGuard } from '../../core/auth.guard';

const routes: Routes = [{ path: '', component: HistoryComponent, canActivate: [AuthGuard] }];

@NgModule({
  declarations: [HistoryComponent],
  imports: [
    CommonModule, RouterModule.forChild(routes), SharedModule,
    MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule,
    MatButtonToggleModule, MatPaginatorModule, MatTableModule, MatChipsModule, MatTooltipModule
  ]
})
export class HistoryModule {}
