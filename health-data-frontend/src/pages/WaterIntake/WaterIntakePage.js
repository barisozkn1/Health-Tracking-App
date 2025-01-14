import React, { useEffect, useState, useContext } from "react";
import axiosInstance from "../../services/axiosConfig";
import WaterChart from "../../components/WaterChart/WaterChart";
import "./WaterIntakePage.css";
import { UserContext } from "../../context/UserContext";
import dropletIcon from "../../assets/droplet.png";

const WaterIntakePage = () => {
  const [data, setData] = useState([]);
  const [summary, setSummary] = useState(null);
  const [formData, setFormData] = useState({
    recordDate: "",
    amount: "",
    target: "",
  });
  const [editingRecord, setEditingRecord] = useState(null);
  const [showTable, setShowTable] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;
  const { user } = useContext(UserContext);

  // Veri çekme fonksiyonu
  const fetchData = async () => {
    if (user && user.id) {
      try {
        const response = await axiosInstance.get(`/water/list/${user.id}`);
        setData(response.data);

        // Özet bilgiyi güncelle
        const summaryResponse = await axiosInstance.get(
          `/water/summary/${user.id}`
        );
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
          await axiosInstance.put(
            `/water/update/${editingRecord.id}`,
            newRecord
          );
          alert("Kayıt başarıyla güncellendi!");
          setEditingRecord(null);
        } else {
          await axiosInstance.post("/water/create", newRecord);
          alert("Yeni kayıt başarıyla eklendi!");
        }
        setFormData({ recordDate: "", amount: "", target: "" });

        // Veri ve özet bilgiyi yeniden yükle
        await fetchData();
      } catch (error) {
        console.error(
          "Kayıt ekleme/güncelleme hatası:",
          error.response || error.message
        );
      }
    }
  };

  const handleEdit = (record) => {
    setEditingRecord(record);
    setFormData({
      recordDate: record.recordDate,
      amount: record.amount,
      target: record.target,
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bu kaydı silmek istediğinize emin misiniz?")) {
      try {
        await axiosInstance.delete(`/water/delete/${id}`, {
          params: { userId: user.id },
        });
        alert("Kayıt başarıyla silindi!");

        // Veri ve özet bilgiyi yeniden yükle
        await fetchData();
      } catch (error) {
        console.error(
          "Kayıt silme hatası:",
          error.response || error.message
        );
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

  return (
    <div className="water-intake-container">
      <div className="header-section">
        <img src={dropletIcon} alt="Water Icon" className="droplet-icon" />
      </div>

      {summary && (
        <div className="summary-section">
          <h3>Özet Bilgiler</h3>
          <p>Haftalık Toplam: {summary.weeklyTotal.toFixed(1)} L</p>
          <p>Aylık Toplam: {summary.monthlyTotal.toFixed(1)} L</p>
          <p>Yıllık Toplam: {summary.yearlyTotal.toFixed(1)} L</p>
          <p>Hedefe Ulaşma Oranı: %{summary.goalAchievementRate.toFixed(1)}</p>
        </div>
      )}

      <div className="form-section">
        <form onSubmit={handleSubmit}>
          <input
            type="date"
            id="recordDate"
            name="recordDate"
            value={formData.recordDate}
            onChange={handleInputChange}
            required
          />
          <input
            type="number"
            id="amount"
            name="amount"
            value={formData.amount}
            onChange={handleInputChange}
            min="0"
            step="0.1"
            placeholder="Miktar (Litre)"
            required
          />
          <input
            type="number"
            id="target"
            name="target"
            value={formData.target}
            onChange={handleInputChange}
            min="0"
            step="0.1"
            placeholder="Hedef (Litre)"
            required
          />
          <button type="submit">{editingRecord ? "Güncelle" : "Ekle"}</button>
        </form>
      </div>

      <div className="chart-section">
        <WaterChart data={data} />
      </div>

      <div className="toggle-table-container">
        <button onClick={toggleTable}>
          {showTable ? "Tabloyu Gizle" : "Tabloyu Göster"}
        </button>
      </div>

      {showTable && (
        <div className={`table-section ${showTable ? "show" : "hide"}`}>
          <table className="water-table">
            <thead>
              <tr>
                <th>Tarih</th>
                <th>Miktar (Litre)</th>
                <th>Hedef (Litre)</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              {currentItems.length > 0 ? (
                currentItems.map((item) => (
                  <tr key={item.id}>
                    <td>{item.recordDate}</td>
                    <td>{parseFloat(item.amount).toFixed(1)} L</td>
                    <td>{parseFloat(item.target).toFixed(1)} L</td>
                    <td>
                      <button className="edit-btn" onClick={() => handleEdit(item)}>Düzenle</button>
                      <button className="delete-btn" onClick={() => handleDelete(item.id)}>Sil</button>
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
                  className={`pagination-button ${
                    currentPage === index + 1 ? "active" : ""
                  }`}
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

export default WaterIntakePage;
