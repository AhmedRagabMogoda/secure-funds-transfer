/**
 * Request DTO for initiating a fund transfer.
 * Sent to POST /api/transfers.
 */
export interface TransferRequest {
  receiverAccountNumber: string;
  amount: number;
}

/**
 * Response DTO returned after a successful transfer.
 */
export interface TransferResponse {
  senderAccountNumber: string;
  receiverAccountNumber: string;
  amount: number;
  newBalance: number;
  status: string;
  createdAt: string;
}

/**
 * Represents a single transaction record in the user's history.
 * The `direction` field is either 'SENT' or 'RECEIVED'.
 */
export interface TransactionResponse {
  id: number;
  senderAccountNumber: string;
  receiverAccountNumber: string;
  amount: number;
  status: string;
  createdAt: string;
  direction: 'SENT' | 'RECEIVED';
}
