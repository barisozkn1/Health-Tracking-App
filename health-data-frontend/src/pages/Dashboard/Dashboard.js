import React, { useContext } from 'react';
import Card from '../../components/Card/Card';
import { UserContext } from '../../context/UserContext';
import './Dashboard.css';

const Dashboard = () => {
  const { user } = useContext(UserContext);

  const modules = [
    { title: 'Su Tüketimi', description: 'Günlük su tüketimini takip et', link: `/water?userId=${user.id}`, icon: '💧', color: ['#56CCF2', '#2F80ED'] },
    { title: 'Kalori Alımı', description: 'Günlük kalori takibi yap', link: `/calorie?userId=${user.id}`, icon: '🔥', color: ['#F2994A', '#F2C94C'] },
    { title: 'Adım Sayacı', description: 'Adım sayını ve hedefini gör', link: `/steps?userId=${user.id}`, icon: '👣', color: ['#9B59B6', '#8E44AD'] },
    { title: 'Kalp Atış Hızı', description: 'Kalp atış hızını kaydet', link: `/heart-rate?userId=${user.id}`, icon: '❤️', color: ['#E74C3C', '#C0392B'] },
    { title: 'Boy ve Kilo', description: 'Boy ve kilo durumunu yönet', link: `/height-weight?userId=${user.id}`, icon: '📏', color: ['#95A5A6', '#7F8C8D'] },
    { title: 'Uyku', description: 'Uyku düzenini takip et', link: `/sleep?userId=${user.id}`, icon: '😴', color: ['#A29BFE', '#6C5CE7'] },
  ];

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <div className="dashboard-icon">💓</div>
        <h1>Sağlıklı Bir Yaşam İçin</h1>
        <p>Hedeflerinizi güvence altına alın ve her adımda daha sağlıklı olun!</p>
      </div>
      <div className="dashboard-grid">
        {modules.map((module, index) => (
          <Card
            key={index}
            title={module.title}
            description={module.description}
            link={module.link}
            icon={module.icon}
            color={module.color}
          />
        ))}
      </div>
    </div>
  );
};

export default Dashboard;
