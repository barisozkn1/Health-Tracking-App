import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Card.css';

const Card = ({ title, description, link, icon, color }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(link);
  };

  return (
    <div
      className="card"
      style={{ background: `linear-gradient(to bottom right, ${color[0]}, ${color[1]})` }}
      onClick={handleClick}
    >
      <div className="card-icon">{icon}</div>
      <h2 className="card-title">{title}</h2>
      <p className="card-description">{description}</p>
      <button className="card-button">Detaylara Git</button>
    </div>
  );
};

export default Card;
