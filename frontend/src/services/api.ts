import { storageService } from './storageService';

const API_BASE_URL = 'http://localhost:8080/api';

interface ApiResponse<T> {
  data: T;
  error?: string;
}

class ApiService {
  private getHeaders(): HeadersInit {
    const token = storageService.getToken();
    return {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
  }

  private async handleResponse<T>(response: Response): Promise<ApiResponse<T>> {
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'An error occurred');
    }
    const data = await response.json();
    return { data };
  }

  async post<T>(endpoint: string, body: any): Promise<ApiResponse<T>> {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: this.getHeaders(),
        body: JSON.stringify(body),
      });
      return this.handleResponse<T>(response);
    } catch (error) {
      return {
        data: null as T,
        error: error instanceof Error ? error.message : 'An error occurred',
      };
    }
  }
}

export const apiService = new ApiService(); 