import React from 'react';
import { Bar } from 'react-chartjs-2';
import './HeartRateChart.css';

const HeartRateChart = ({ barChartData }) => {
  return (
    <div className="heart-rate-chart">
      <h3>Günlük Kalp Atış Hızı</h3>
      <Bar data={barChartData} options={{ responsive: true }} />
    </div>
  );
};

export default HeartRateChart;
