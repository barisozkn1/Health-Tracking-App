import axiosInstance from './axiosConfig';

// Giriş işlemi
export const login = async (email, password) => {
  try {
    const response = await axiosInstance.post('/auth/login', { email, password });
    const { token, userName, userId } = response.data;

    // Kullanıcı bilgilerini kaydet
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify({ id: userId, userName }));

    return { token, userName, userId }; // Geri dönen veri
  } catch (error) {
    console.error('Login error:', error);
    throw error;
  }
};

// Çıkış işlemi
export const logout = async () => {
  try {
    await axiosInstance.post(
      '/auth/logout',
      {},
      {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem('token')}`,
        },
      }
    );
    sessionStorage.clear(); // Oturum bilgilerini temizle
    window.location.href = '/login'; // Login sayfasına yönlendir
  } catch (error) {
    console.error('Logout error:', error);
    throw error;
  }
};


// Kayıt işlemi
export const register = async (userData) => {
  try {
    const response = await axiosInstance.post('/auth/register', userData);
    return response.data;
  } catch (error) {
    console.error('Register error:', error.response || error.message);
    throw error;
  }
};

// Şifre sıfırlama token gönderme
export const sendResetToken = async (email) => {
  try {
    const response = await axiosInstance.post('/psword/send-reset-token', {
      email,
    });
    return response.data;
  } catch (error) {
    console.error('Password reset token error:', error);
    throw error;
  }
};

// Oturum doğrulama
export const validateSession = async () => {
  try {
    const response = await axiosInstance.get('/auth/validate-session', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error('Session validation error:', error);
    throw error;
  }
};
