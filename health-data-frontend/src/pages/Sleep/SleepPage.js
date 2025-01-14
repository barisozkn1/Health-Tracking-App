import React, { useEffect, useState, useContext } from "react";
import axiosInstance from "../../services/axiosConfig";
import SleepChart from "../../components/SleepChart/SleepChart";
import "./SleepPage.css";
import { UserContext } from "../../context/UserContext";
import sleepingIcon from "../../assets/sleeping.png"; // Imported the sleeping icon

const SleepPage = () => {
  const [data, setData] = useState([]);
  const [summary, setSummary] = useState(null);
  const [formData, setFormData] = useState({ recordDate: "", duration: "", target: "" });
  const [editingRecord, setEditingRecord] = useState(null);
  const [showTable, setShowTable] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;
  const { user } = useContext(UserContext);

  const fetchData = async () => {
    if (user && user.id) {
      try {
        const response = await axiosInstance.get(`/sleep/list/${user.id}`);
        setData(response.data);

        const summaryResponse = await axiosInstance.get(`/sleep/summary/${user.id}`);
        setSummary(summaryResponse.data);
      } catch (error) {
        console.error("Veri çekme hatası:", error.response || error.message);
      }
    }
  };

  useEffect(() => {
    fetchData();
  }, [user]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (user && user.id) {
      const newRecord = { ...formData, userId: user.id };
      try {
        if (editingRecord) {
          await axiosInstance.put(`/sleep/update/${editingRecord.id}`, newRecord);
          alert("Kayıt başarıyla güncellendi!");
          setEditingRecord(null);
        } else {
          await axiosInstance.post("/sleep/create", newRecord);
          alert("Yeni kayıt başarıyla eklendi!");
        }
        setFormData({ recordDate: "", duration: "", target: "" });
        await fetchData();
      } catch (error) {
        console.error("Kayıt ekleme/güncelleme hatası:", error.response || error.message);
      }
    }
  };

  const handleEdit = (record) => {
    setEditingRecord(record);
    setFormData({
      recordDate: record.recordDate,
      duration: record.duration,
      target: record.target,
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bu kaydı silmek istediğinize emin misiniz?")) {
      try {
        await axiosInstance.delete(`/sleep/delete/${id}`, {
          params: { userId: user.id },
        });
        alert("Kayıt başarıyla silindi!");
        await fetchData();
      } catch (error) {
        console.error("Kayıt silme hatası:", error.response || error.message);
        alert("Kayıt silinemedi!");
      }
    }
  };

  const toggleTable = () => {
    setShowTable((prev) => !prev);
  };

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = data.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(data.length / itemsPerPage);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const barChartData = {
    labels: ["Haftalık", "Aylık", "Yıllık"],
    datasets: [
      {
        label: "Toplam Uyku Süresi (Saat)",
        data: summary ? [summary.weeklyTotal, summary.monthlyTotal, summary.yearlyTotal] : [0, 0, 0],
        backgroundColor: "rgba(75, 192, 192, 0.6)",
        borderColor: "rgba(75, 192, 192, 1)",
        borderWidth: 1,
      },
    ],
  };

  const lineChartData = {
    labels: data.map((item) => item.recordDate),
    datasets: [
      {
        label: "Uyku Süresi (Saat)",
        data: data.map((item) => item.duration),
        borderColor: "#4caf50",
        backgroundColor: "rgba(76, 175, 80, 0.3)",
        fill: true,
      },
    ],
  };

  const gaugeValue = summary ? summary.goalAchievementRate : 0;

  return (
    <div className="sleep-page-container">
      <div className="header-container">
        <img src={sleepingIcon} alt="Uyku İkonu" className="sleeping-icon" />
      </div>

      {summary && (
        <div className="summary-section">
          <h3>Özet Bilgiler</h3>
          <p>Haftalık Toplam: {summary.weeklyTotal.toFixed(1)} saat</p>
          <p>Aylık Toplam: {summary.monthlyTotal.toFixed(1)} saat</p>
          <p>Yıllık Toplam: {summary.yearlyTotal.toFixed(1)} saat</p>
          <p>Hedefe Ulaşma Oranı: %{summary.goalAchievementRate.toFixed(2)}</p>
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
            name="duration"
            value={formData.duration}
            onChange={handleInputChange}
            placeholder="Uyku Süresi (Saat)"
            required
          />
          <input
            type="number"
            name="target"
            value={formData.target}
            onChange={handleInputChange}
            placeholder="Hedef (Saat)"
            required
          />
          <button type="submit">{editingRecord ? "Güncelle" : "Ekle"}</button>
        </form>
      </div>

      <SleepChart barChartData={barChartData} lineChartData={lineChartData} gaugeValue={gaugeValue} />

      <div className="toggle-table-container">
        <button onClick={toggleTable}>{showTable ? "Tabloyu Gizle" : "Tabloyu Göster"}</button>
      </div>

      {showTable && (
        <div className={`table-section ${showTable ? 'show' : 'hide'}`}>
          <table className="sleep-table">
            <thead>
              <tr>
                <th>Tarih</th>
                <th>Uyku Süresi (Saat)</th>
                <th>Hedef (Saat)</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              {currentItems.length > 0 ? (
                currentItems.map((item) => (
                  <tr key={item.id}>
                    <td>{item.recordDate}</td>
                    <td>{item.duration.toFixed(1)} saat</td>
                    <td>{item.target.toFixed(1)} saat</td>
                    <td>
                      <button className="edit-btn" onClick={() => handleEdit(record)}>Düzenle</button>
                      <button className="delete-btn" onClick={() => handleDelete(record.id)}>Sil</button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4">Veri bulunamadı</td>
                </tr>
              )}
            </tbody>
          </table>
          {data.length > itemsPerPage && (
            <div className="pagination">
              {Array.from({ length: totalPages }, (_, index) => (
                <button
                  key={index}
                  className={`pagination-button ${currentPage === index + 1 ? "active" : ""}`}
                  onClick={() => handlePageChange(index + 1)}
                >
                  {index + 1}
                </button>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default SleepPage;
