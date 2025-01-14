import React from 'react';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, ArcElement, Tooltip, Legend } from 'chart.js';
import './StepChart.css';

ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Tooltip, Legend);

const StepChart = ({ barChartData, pieChartData }) => {
  // Bar grafiği için yapılandırma
  const barOptions = {
    responsive: true,
    plugins: {
      legend: {
        display: false,
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: 'Tarih',
        },
      },
      y: {
        title: {
          display: true,
          text: 'Adım Sayısı',
        },
        min: 0,
      },
    },
  };

  // Pasta grafiği için yapılandırma
  const pieOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'bottom',
      },
      tooltip: {
        callbacks: {
          label: (context) => `${context.label}: ${context.raw.toFixed(2)}%`,
        },
      },
    },
  };

  return (
    <div className="step-chart-container">
      <div className="chart-item">
        <h3>Günlük Adım Sayısı</h3>
        <Bar data={barChartData} options={barOptions} />
      </div>
      <div className="chart-item">
        <h3>Hedef Ulaşma Oranı</h3>
        <Pie data={pieChartData} options={pieOptions} />
      </div>
    </div>
  );
};

export default StepChart;
