import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';
import { login, sendResetToken } from '../../services/authService';
import { UserContext } from '../../context/UserContext';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [resetEmail, setResetEmail] = useState('');
  const [error, setError] = useState('');
  const [resetMessage, setResetMessage] = useState('');
  const [showResetForm, setShowResetForm] = useState(false);
  const { login: contextLogin } = useContext(UserContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await login(email, password);
      contextLogin({ id: data.userId, userName: data.userName, token: data.token });
      navigate('/dashboard');
    } catch (err) {
      console.error('Login hatası:', err);
      setError('Giriş başarısız! Lütfen bilgilerinizi kontrol edin.');
    }
  };

  const handleResetRequest = async (e) => {
    e.preventDefault();
    try {
      await sendResetToken(resetEmail);
      setResetMessage('Şifre sıfırlama bağlantısı e-posta adresinize gönderildi.');
      setResetEmail('');
      setShowResetForm(false);
    } catch (err) {
      setError('Şifre sıfırlama işlemi başarısız! Lütfen geçerli bir e-posta girin.');
    }
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h2>Giriş Yap</h2>
        {error && <p className="error-message">{error}</p>}
        {resetMessage && <p className="success-message">{resetMessage}</p>}

        {!showResetForm ? (
          <>
            <form onSubmit={handleSubmit}>
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
              <input
                type="password"
                placeholder="Parola"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button type="submit" className="login-button">
                Giriş
              </button>
            </form>
            <p>
              Henüz üyeliğin yok mu?{' '}
              <span
                className="link register-link"
                onClick={() => navigate('/register')}
              >
                Üye Ol
              </span>
            </p>
            <p>
              Şifrenizi mi unuttunuz?{' '}
              <span
                className="link forgot-password-link"
                onClick={() => setShowResetForm(true)}
              >
                Şifremi Unuttum
              </span>
            </p>
          </>
        ) : (
          <form onSubmit={handleResetRequest}>
            <input
              type="email"
              placeholder="E-posta adresiniz"
              value={resetEmail}
              onChange={(e) => setResetEmail(e.target.value)}
              required
            />
            <button type="submit" className="login-button">
              Şifreyi Sıfırla
            </button>
            <button
              type="button"
              className="cancel-button"
              onClick={() => setShowResetForm(false)}
            >
              İptal
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default Login;
