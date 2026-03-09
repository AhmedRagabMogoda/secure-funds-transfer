/**
 * Mirrors the backend AccountResponse DTO.
 * Returned from GET /api/accounts/me.
 */
export interface AccountResponse {
  accountNumber: string;
  balance: number;
  ownerUsername: string;
  createdAt: string;
}
