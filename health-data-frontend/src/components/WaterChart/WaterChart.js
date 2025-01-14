import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import './WaterChart.css';
// Gerekli Chart.js bileşenlerini kaydet
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const WaterChart = ({ data }) => {
  const chartData = {
    labels: data.map((item) => item.recordDate), // Tarihleri ekle
    datasets: [
      {
        label: 'Su Tüketimi (Litre)',
        data: data.map((item) => item.amount), // Su tüketim miktarlarını ekle
        backgroundColor: 'rgba(75, 192, 192, 0.6)', // Bar rengi
        borderColor: 'rgba(75, 192, 192, 1)', // Bar kenarlık rengi
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { display: true, position: 'top' }, // Efsane konumu
      title: {
        display: true,
        text: 'Su Tüketimi Özeti', // Başlık
      },
    },
    scales: {
      x: { 
        title: { display: true, text: 'Tarih' }, // X ekseni başlığı
      },
      y: { 
        title: { display: true, text: 'Litre' }, // Y ekseni başlığı
        beginAtZero: true, // Sıfırdan başlat
      },
    },
  };

  return <Bar data={chartData} options={options} />;
};

export default WaterChart;
