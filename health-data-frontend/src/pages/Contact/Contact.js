import React, { useState } from 'react';
import './Contact.css';
import contactImage from '../../assets/contact-background.jpg';

const Contact = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    message: '',
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Burada formu backend'e gönderebilirsiniz
    alert('Mesajınız gönderildi. Teşekkür ederiz!');
    setFormData({ name: '', email: '', message: '' });
  };

  return (
    <div>
      {/* Hero Section */}
      <div className="hero-section" style={{ backgroundImage: `url(${contactImage})` }}>
        <h1>Bizimle İletişime Geçin!</h1>
        <p>Size yardımcı olmaktan mutluluk duyarız. Sorularınız için bize ulaşın.</p>
      </div>

      {/* Contact Section */}
      <div className="contact-content">
        <div className="contact-form">
          <h2>Bize Ulaşın</h2>
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              name="name"
              placeholder="Adınız"
              value={formData.name}
              onChange={handleChange}
              required
            />
            <input
              type="email"
              name="email"
              placeholder="E-posta Adresiniz"
              value={formData.email}
              onChange={handleChange}
              required
            />
            <textarea
              name="message"
              placeholder="Mesajınız"
              value={formData.message}
              onChange={handleChange}
              required
            ></textarea>
            <button type="submit" className="send-button">Gönder</button>
          </form>
        </div>
        <div className="contact-info">
          <h2>İletişim Bilgileri</h2>
          <p>Telefon: +90 543 950 66 64</p>
          <p>E-posta: destek@sagliktakip.com</p>
          <p>Adres: İstanbul, Türkiye</p>
          <div className="social-media">
            <a href="https://linkedin.com" target="_blank" rel="noreferrer">LinkedIn</a>
            <a href="https://twitter.com" target="_blank" rel="noreferrer">Twitter</a>
            <a href="https://instagram.com" target="_blank" rel="noreferrer">Instagram</a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Contact;
