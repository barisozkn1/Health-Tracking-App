import React from 'react';
import { useNavigate } from 'react-router-dom';
import './About.css';
import heroImage from '../../assets/your-hero-image.jpg';
import aboutImage from '../../assets/about-image.jpg';

const About = () => {
  const navigate = useNavigate();

  const handleStartNow = () => {
    navigate('/login'); // Login sayfasına yönlendir
  };

  return (
    <div className="about-container">
      <div
        className="about-hero"
        style={{ backgroundImage: `url(${heroImage})` }}
      >
        <h1>Sağlık Takip Platformu'na Hoş Geldiniz!</h1>
        <p>Sağlıklı yaşam tarzınıza rehberlik etmek için buradayız.</p>
      </div>

      <div className="about-content">
        <div className="about-text">
          <h2>Sağlıklı bir yaşama adım atın!</h2>
          <p>
            Sağlık Takip Platformu, kullanıcıların günlük sağlık hedeflerini takip
            etmelerine yardımcı olmak için tasarlanmıştır. Su tüketiminizi yönetin,
            kalorilerinizi takip edin ve sağlıklı yaşam tarzınızı düzenleyin.
          </p>
          <h2>Hedeflerinizi gerçekleştirin!</h2>
          <p>
            Modern araçlarımızla sağlığınızı kontrol altına alın ve hayatınıza yeni
            bir yön verin.
          </p>
        </div>
        <div className="about-image">
          <img src={aboutImage} alt="About Us" />
        </div>
      </div>
    </div>
  );
};

export default About;
