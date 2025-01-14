import axios from 'axios';

// Base URL ayarı
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/rest/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor: Her isteğe token ekleme
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    console.log('Request URL:', config.url); // Hangi URL'ye istek yapıldığını kontrol et
    console.log('Token:', token); // Token'ı kontrol et
    if (!config.url.includes('/auth/register') && !config.url.includes('/auth/login')) {
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
        console.log('Authorization Header Set:', config.headers['Authorization']); // Header'ı doğrula
      } else {
        console.warn('Token bulunamadı!');
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor: Hataları yönetme
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        console.warn('Yetkisiz erişim: Kullanıcı oturumu sona ermiş olabilir.');
        localStorage.clear(); // Tüm kullanıcı verilerini temizle
        window.location.href = '/login'; // Login sayfasına yönlendir
      } else if (error.response.status === 403) {
        console.error('Erişim yetkiniz yok.');
      }
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
