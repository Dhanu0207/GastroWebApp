import { create } from 'zustand';
import { User } from '../types';
import { authApi } from '../api/endpoints';

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<boolean>;
  register: (fullName: string, email: string, password: string, phoneNumber?: string) => Promise<boolean>;
  logout: () => void;
  checkAuth: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: {
    id: 1,
    email: 'alex@gastro.com',
    fullName: 'Alex Chef',
    role: 'ROLE_USER',
  },
  token: localStorage.getItem('gastro_jwt_token') || 'mock_token',
  isAuthenticated: true,
  isLoading: false,

  login: async (email, password) => {
    set({ isLoading: true });
    try {
      const res = await authApi.login({ email, password });
      localStorage.setItem('gastro_jwt_token', res.token);
      set({ user: res.user, token: res.token, isAuthenticated: true, isLoading: false });
      return true;
    } catch {
      set({ isLoading: false });
      return false;
    }
  },

  register: async (fullName, email, password, phoneNumber) => {
    set({ isLoading: true });
    try {
      const res = await authApi.register({ fullName, email, password, phoneNumber });
      localStorage.setItem('gastro_jwt_token', res.token);
      set({ user: res.user, token: res.token, isAuthenticated: true, isLoading: false });
      return true;
    } catch {
      set({ isLoading: false });
      return false;
    }
  },

  logout: () => {
    localStorage.removeItem('gastro_jwt_token');
    set({ user: null, token: null, isAuthenticated: false });
  },

  checkAuth: () => {
    const token = localStorage.getItem('gastro_jwt_token');
    if (token) {
      set({ isAuthenticated: true });
    }
  },
}));
