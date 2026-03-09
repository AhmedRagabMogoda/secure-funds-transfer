import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { UserProfileResponse } from '../models/user-profile.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly API_URL = '/api/users';
  constructor(private http: HttpClient) {}

  getProfile(): Observable<ApiResponse<UserProfileResponse>> {
    return this.http.get<ApiResponse<UserProfileResponse>>(`${this.API_URL}/profile`);
  }
}
