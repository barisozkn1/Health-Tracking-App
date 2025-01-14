import React from 'react';
import { Bar, Pie, Line } from 'react-chartjs-2';
import 'chart.js/auto';
import './CalorieChart.css';

const CalorieChart = ({ data }) => {
  // Bar Chart: Günlük Kalori Alımı
  const barData = {
    labels: data.map((item) => item.recordDate),
    datasets: [
      {
        label: 'Kalori Alımı (kcal)',
        data: data.map((item) => item.amount),
        backgroundColor: 'rgba(54, 162, 235, 0.6)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
      },
    ],
  };

  // Pie Chart: Hedefe Ulaşma Oranı
  const totalCalories = data.reduce((sum, item) => sum + item.amount, 0);
  const totalTargets = data.reduce((sum, item) => sum + item.target, 0);
  const pieData = {
    labels: ['Gerçekleşen', 'Eksik'],
    datasets: [
      {
        data: [totalCalories, Math.max(totalTargets - totalCalories, 0)],
        backgroundColor: ['rgba(75, 192, 192, 0.6)', 'rgba(255, 99, 132, 0.6)'],
        borderColor: ['rgba(75, 192, 192, 1)', 'rgba(255, 99, 132, 1)'],
        borderWidth: 1,
      },
    ],
  };

  // Line Chart: Zaman İçinde Kalori Alımı
  const lineData = {
    labels: data.map((item) => item.recordDate),
    datasets: [
      {
        label: 'Kalori Alımı (kcal)',
        data: data.map((item) => item.amount),
        fill: false,
        borderColor: 'rgba(153, 102, 255, 1)',
        backgroundColor: 'rgba(153, 102, 255, 0.6)',
        tension: 0.1,
      },
    ],
  };

  return (
    <div className="calorie-chart-container">
      <div className="chart-item bar-chart">
        <h3>Günlük Kalori Alımı</h3>
        <Bar data={barData} />
      </div>
      <div className="chart-item pie-chart">
        <h3>Hedefe Ulaşma Oranı</h3>
        <Pie data={pieData} />
      </div>
      <div className="chart-item line-chart">
        <h3>Zaman İçinde Kalori Alımı</h3>
        <Line data={lineData} />
      </div>
    </div>
  );
};

export default CalorieChart;
