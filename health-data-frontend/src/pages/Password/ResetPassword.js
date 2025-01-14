import React, { useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import './ResetPassword.css';
import axiosInstance from '../../services/axiosConfig';

const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [newPassword, setNewPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const token = searchParams.get('token'); // URL'den token'ı al

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Backend'e şifre sıfırlama isteği gönder
      await axiosInstance.post('/psword/reset-password', {
        token,
        newPassword,
      });
      setSuccess(true);
      setTimeout(() => navigate('/login'), 3000); // 3 saniye sonra login sayfasına yönlendir
    } catch (err) {
      setError('Şifre sıfırlama başarısız! Lütfen tekrar deneyin.');
    }
  };

  return (
    <div className="reset-password-container">
      <div className="reset-password-form">
        <h2>Şifre Sıfırla</h2>
        {success ? (
          <p className="success-message">Şifreniz başarıyla güncellendi! Giriş sayfasına yönlendiriliyorsunuz...</p>
        ) : (
          <>
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleSubmit}>
              <input
                type="password"
                placeholder="Yeni Şifre"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
              <button type="submit" className="reset-button">
                Şifreyi Güncelle
              </button>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default ResetPassword;
