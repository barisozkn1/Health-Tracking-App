import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Register.css';
import { register } from '../../services/authService';

const Register = () => {
  const [formData, setFormData] = useState({
    userName: '',
    email: '',
    password: '',
    gender: '',
  });
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');

    try {
      await register(formData);
      setSuccessMessage('Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz...');
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      if (err.response && err.response.data && err.response.data.message) {
        setError(err.response.data.message);
      } else {
        setError('Kayıt işlemi başarısız! Lütfen bilgilerinizi kontrol edin.');
      }
    }
  };

  return (
    <div className="register-container">
      <div className="register-form">
        <h2>Üye Ol</h2>
        {error && <p className="error-message">{error}</p>}
        {successMessage && <p className="success-message">{successMessage}</p>}
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="userName"
            placeholder="Ad Soyad"
            value={formData.userName}
            onChange={handleChange}
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Parola"
            value={formData.password}
            onChange={handleChange}
            required
          />
          <select
            name="gender"
            value={formData.gender}
            onChange={handleChange}
            required
          >
            <option value="">Cinsiyet</option>
            <option value="MALE">Erkek</option>
            <option value="FEMALE">Kadın</option>
          </select>
          <button type="submit" className="register-button">Kayıt Ol</button>
        </form>
      </div>
    </div>
  );
};

export default Register;
