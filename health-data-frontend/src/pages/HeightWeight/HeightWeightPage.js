import React, { useEffect, useState, useContext } from "react";
import axiosInstance from "../../services/axiosConfig";
import HeightWeightChart from "../../components/HeightWeightChart/HeightWeightChart";
import "./HeightWeightPage.css";
import { UserContext } from "../../context/UserContext";
import bmiIcon from "../../assets/bmi.png";

const HeightWeightPage = () => {
  const { user } = useContext(UserContext);
  const [data, setData] = useState([]);
  const [summary, setSummary] = useState(null);
  const [formData, setFormData] = useState({
    recordDate: "",
    height: "",
    weight: "",
  });
  const [editingRecord, setEditingRecord] = useState(null);
  const [showTable, setShowTable] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const recordsPerPage = 5;

  useEffect(() => {
    if (user && user.id) {
      fetchHeightWeightData();
      fetchSummaryData();
    }
  }, [user]);

  const fetchHeightWeightData = async () => {
    try {
      const response = await axiosInstance.get(`/height-weight/list/${user.id}`);
      setData(response.data);
    } catch (error) {
      console.error("Boy-kilo verileri alınamadı:", error);
    }
  };

  const fetchSummaryData = async () => {
    try {
      const response = await axiosInstance.get(`/height-weight/summary/${user.id}`);
      setSummary(response.data);
    } catch (error) {
      console.error("Özet verisi alınamadı:", error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newRecord = { ...formData, userId: user.id };

    try {
      if (editingRecord) {
        await axiosInstance.put(`/height-weight/update/${editingRecord.id}`, newRecord);
        setEditingRecord(null);
      } else {
        await axiosInstance.post("/height-weight/create", newRecord);
      }
      setFormData({ recordDate: "", height: "", weight: "" });
      fetchHeightWeightData();
      fetchSummaryData();
    } catch (error) {
      console.error("Kayıt işlenirken hata oluştu:", error);
    }
  };

  const handleEdit = (record) => {
    setEditingRecord(record);
    setFormData({
      recordDate: record.recordDate,
      height: record.height,
      weight: record.weight,
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bu kaydı silmek istediğinize emin misiniz?")) {
      try {
        await axiosInstance.delete(`/height-weight/delete/${id}`, {
          params: { userId: user.id },
        });
        fetchHeightWeightData();
        fetchSummaryData();
      } catch (error) {
        console.error("Kayıt silinirken hata oluştu:", error);
      }
    }
  };

  const toggleTable = () => {
    setShowTable((prev) => !prev);
  };

  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = data.slice(indexOfFirstRecord, indexOfLastRecord);
  const totalPages = Math.ceil(data.length / recordsPerPage);

  const lineChartData = {
    labels: data.map((item) => item.recordDate),
    datasets: [
      {
        label: "Boy (cm)",
        data: data.map((item) => item.height),
        borderColor: "#42a5f5",
        backgroundColor: "rgba(66, 165, 245, 0.5)",
        fill: true,
      },
      {
        label: "Kilo (kg)",
        data: data.map((item) => item.weight),
        borderColor: "#ff7043",
        backgroundColor: "rgba(255, 112, 67, 0.5)",
        fill: true,
      },
    ],
  };

  return (
    <div className="height-weight-container">
      <div className="header-section">
        <img src={bmiIcon} alt="BMI Icon" className="bmi-icon" />
      </div>

      {summary && (
        <div className="summary-section">
          <h3>Özet Bilgiler</h3>
          <p>BMI: {summary.bmi.toFixed(2)}</p>
          <p>Kategori: {summary.bmiCategory}</p>
          <p>İdeal Kilo Aralığı: {summary.idealWeightLow.toFixed(2)} kg - {summary.idealWeightHigh.toFixed(2)} kg</p>
        </div>
      )}

      <div className="form-section">
        <form onSubmit={handleSubmit}>
          <input
            type="date"
            name="recordDate"
            value={formData.recordDate}
            onChange={handleInputChange}
            required
          />
          <input
            type="number"
            name="height"
            value={formData.height}
            onChange={handleInputChange}
            placeholder="Boy (cm)"
            required
          />
          <input
            type="number"
            name="weight"
            value={formData.weight}
            onChange={handleInputChange}
            placeholder="Kilo (kg)"
            required
          />
          <button type="submit">{editingRecord ? "Güncelle" : "Ekle"}</button>
        </form>
      </div>

      <HeightWeightChart
        lineChartData={lineChartData}
        bmiValue={summary ? summary.bmi.toFixed(2) : 0}
        bmiCategory={summary ? summary.bmiCategory : "Bilinmiyor"}
      />

      <div className="toggle-table-container">
        <button onClick={toggleTable}>
          {showTable ? "Tabloyu Gizle" : "Tabloyu Göster"}
        </button>
      </div>

      {showTable && (
        <div className={`table-section ${showTable ? "show" : "hide"}`}>
          <table>
            <thead>
              <tr>
                <th>Tarih</th>
                <th>Boy (cm)</th>
                <th>Kilo (kg)</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              {currentRecords.map((record, index) => (
                <tr key={index}>
                  <td>{record.recordDate}</td>
                  <td>{record.height} cm</td>
                  <td>{record.weight} kg</td>
                  <td>
                    <button className="edit-btn" onClick={() => handleEdit(record)}>Düzenle</button>
                    <button className="delete-btn" onClick={() => handleDelete(record.id)}>Sil</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="pagination">
            {Array.from({ length: totalPages }, (_, index) => (
              <button
                key={index}
                onClick={() => setCurrentPage(index + 1)}
                className={currentPage === index + 1 ? "active" : ""}
              >
                {index + 1}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default HeightWeightPage;
