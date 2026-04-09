import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { auth as authApi } from '../services/api';

interface AuthCtx { token: string | null; isAuthenticated: boolean; login: (email: string, password: string) => Promise<void>; logout: () => void; }
const AuthContext = createContext<AuthCtx>(null!);
export const useAuth = () => useContext(AuthContext);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
  const isAuthenticated = !!token;

  const login = async (email: string, password: string) => {
    const data = await authApi.login(email, password);
    const t = (data as any).accessToken;
    localStorage.setItem('token', t);
    setToken(t);
  };

  const logout = () => { localStorage.removeItem('token'); setToken(null); };

  return <AuthContext.Provider value={{ token, isAuthenticated, login, logout }}>{children}</AuthContext.Provider>;
}
