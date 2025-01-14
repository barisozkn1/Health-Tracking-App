import React, { createContext, useState, useEffect } from 'react';
import { validateSession } from '../services/authService';

export const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const storedUser = sessionStorage.getItem('user'); // sessionStorage kullanımı
    return storedUser ? JSON.parse(storedUser) : null;
  });

  const login = (userData) => {
    setUser(userData);
    sessionStorage.setItem('user', JSON.stringify(userData)); // Kullanıcı bilgilerini sakla
    sessionStorage.setItem('token', userData.token); // Token'ı sakla
  };

  const logout = () => {
    setUser(null);
    sessionStorage.clear(); // Tüm sessionStorage'ı temizle
  };

  const verifySession = async () => {
    const token = sessionStorage.getItem('token'); // Token'ı sessionStorage'dan al
    if (token) {
      try {
        const sessionData = await validateSession(); // Backend doğrulaması
        if (sessionData?.id && sessionData?.userName && sessionData?.email) {
          const updatedUser = {
            id: sessionData.id,
            userName: sessionData.userName,
            email: sessionData.email,
            gender: sessionData.gender || 'Bilinmiyor',
            token,
          };
          login(updatedUser);
        } else {
          console.error('Eksik kullanıcı verisi.');
          logout();
        }
      } catch (error) {
        console.error('Oturum doğrulama başarısız:', error.message || error);
        logout();
      }
    }
  };

  useEffect(() => {
    verifySession();

    // Tarayıcı kapatıldığında oturumu sıfırla
    const handleUnload = () => logout();
    window.addEventListener('beforeunload', handleUnload);

    return () => {
      window.removeEventListener('beforeunload', handleUnload);
    };
  }, []);

  return (
    <UserContext.Provider value={{ user, login, logout, verifySession }}>
      {children}
    </UserContext.Provider>
  );
};
