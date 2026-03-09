/**
 * Mirrors the backend ApiResponse<T> wrapper.
 * Every API call returns this structure regardless of success or failure.
 */
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}
