import React, { createContext, useContext, useState, useEffect } from 'react';
import { AuthContextType, User, LoginCredentials, RegisterCredentials } from '../types/auth';
import { authService } from '../services/authService';
import { storageService } from '../services/storageService';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    // Initialize auth state from storage
    const storedToken = storageService.getToken();
    const storedUser = storageService.getUser();

    if (storedToken && storedUser) {
      setToken(storedToken);
      setUser(storedUser);
    }
  }, []);

  const handleAuthResponse = (data: { token: string; id: number; username: string; email: string; role: string }) => {
    const userData: User = {
      id: data.id,
      username: data.username,
      email: data.email,
      role: data.role
    };

    // Store in localStorage
    storageService.setToken(data.token);
    storageService.setUser(userData);

    // Update state
    setToken(data.token);
    setUser(userData);
  };

  const login = async (credentials: LoginCredentials) => {
    try {
      console.log("credentials", credentials);
      
      const data = await authService.login(credentials);
      console.log("data", data);
      
      handleAuthResponse(data);
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  };

  const register = async (credentials: RegisterCredentials) => {
    try {
      const data = await authService.register(credentials);
      handleAuthResponse(data);
    } catch (error) {
      console.error('Registration error:', error);
      throw error;
    }
  };

  const logout = () => {
    storageService.clearAuth();
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        login,
        register,
        logout,
        isAuthenticated: !!token,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}; 