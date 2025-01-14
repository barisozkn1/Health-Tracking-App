import React from "react";
import { Bar, Line } from "react-chartjs-2";
import GaugeChart from "react-gauge-chart";
import "./SleepChart.css";

const SleepChart = ({ barChartData, lineChartData, gaugeValue }) => {
  // Bar Chart options
  const barChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: true, position: "top" },
      title: { display: true, text: "Toplam Uyku Süresi (Haftalık, Aylık, Yıllık)" },
    },
    scales: {
      x: { title: { display: true, text: "Zaman Aralığı" } },
      y: { title: { display: true, text: "Saat" }, beginAtZero: true },
    },
  };

  // Line Chart options
  const lineChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: true, position: "top" },
      title: { display: true, text: "Zamanla Uyku Süresi Değişimi" },
    },
    scales: {
      x: { title: { display: true, text: "Tarih" } },
      y: { title: { display: true, text: "Saat" }, beginAtZero: true },
    },
  };

  return (
    <div className="sleep-chart-container">
      <div className="chart-row">
        <div className="chart-item">
          <h3>Toplam Uyku Süresi</h3>
          <Bar data={barChartData} options={barChartOptions} />
        </div>
        <div className="chart-item">
          <h3>Uyku Süresi Değişimi</h3>
          <Line data={lineChartData} options={lineChartOptions} />
        </div>
      </div>
      <div className="chart-row center-chart">
        <div className="chart-item">
          <h3>Hedefe Ulaşma Oranı</h3>
          <GaugeChart
            id="sleep-gauge-chart"
            nrOfLevels={4}
            colors={["#00e676", "#ffea00", "#ff9100", "#d50000"]}
            percent={gaugeValue / 100} // Value 0-1 arasında olmalı
            arcWidth={0.3}
            textColor="#34495e"
          />
          <p className="gauge-label">Hedef Tamamlama: %{gaugeValue.toFixed(2)}</p>
        </div>
      </div>
    </div>
  );
};

export default SleepChart;
