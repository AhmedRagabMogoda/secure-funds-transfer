/**
 * Mirrors the backend AuthResponse DTO.
 * Returned from POST /api/auth/login and POST /api/auth/refresh.
 */
export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  role: string;
}
