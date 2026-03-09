import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',     loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule) },
  { path: 'dashboard', loadChildren: () => import('./pages/dashboard/dashboard.module').then(m => m.DashboardModule) },
  { path: 'transfer',  loadChildren: () => import('./pages/transfer/transfer.module').then(m => m.TransferModule) },
  { path: 'history',   loadChildren: () => import('./pages/history/history.module').then(m => m.HistoryModule) },
  { path: 'profile',   loadChildren: () => import('./pages/profile/profile.module').then(m => m.ProfileModule) },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
