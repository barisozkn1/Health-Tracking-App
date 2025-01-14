import React from "react";
import { Line } from "react-chartjs-2";
import GaugeChart from "react-gauge-chart";
import "./HeightWeightChart.css";

const HeightWeightChart = ({ lineChartData, bmiValue, bmiCategory }) => {
  // Line Chart options
  const lineChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: true, position: "top" },
      title: { display: true, text: "Zamanla Boy ve Kilo Değişimi" },
    },
    scales: {
      x: { title: { display: true, text: "Tarih" } },
      y: { title: { display: true, text: "Değer (cm / kg)" }, beginAtZero: true },
    },
  };

  return (
    <div className="height-weight-chart-container">
      <div className="chart-item">
        <h3>Boy ve Kilo Değişimi</h3>
        <Line data={lineChartData} options={lineChartOptions} />
      </div>
      <div className="chart-item">
        <h3>BMI Değeri</h3>
        <GaugeChart
          id="bmi-gauge"
          nrOfLevels={4}
          colors={["#FF5F6D", "#FFC371", "#47B39C", "#2C9C69"]}
          arcWidth={0.3}
          percent={bmiValue / 40} // BMI değerini ölçeğe göre normalize ediyoruz
          textColor="#000"
        />
        <p className="bmi-category">Kategori: {bmiCategory}</p>
      </div>
    </div>
  );
};

export default HeightWeightChart;
