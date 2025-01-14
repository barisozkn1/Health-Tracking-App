import React, { useEffect, useState, useContext } from 'react';
import axiosInstance from '../../services/axiosConfig';
import CalorieChart from '../../components/CalorieChart/CalorieChart';
import './CalorieIntakePage.css';
import { UserContext } from '../../context/UserContext';
import caloriesIcon from '../../assets/calories.png';

const CalorieIntakePage = () => {
  const [data, setData] = useState([]);
  const [summary, setSummary] = useState(null);
  const [formData, setFormData] = useState({ recordDate: '', amount: '', target: '' });
  const [editingRecord, setEditingRecord] = useState(null);
  const [showTable, setShowTable] = useState(true);
  const { user } = useContext(UserContext);
  const [currentPage, setCurrentPage] = useState(1);
  const recordsPerPage = 5;

  const fetchData = async () => {
    if (user && user.id) {
      try {
        const response = await axiosInstance.get(`/calorie/list/${user.id}`);
        setData(response.data);

        const summaryResponse = await axiosInstance.get(`/calorie/summary/${user.id}`);
        setSummary(summaryResponse.data);
      } catch (error) {
        console.error('Veri çekme hatası:', error.response || error.message);
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
          await axiosInstance.put(`/calorie/update/${editingRecord.id}`, newRecord);
          alert('Kayıt başarıyla güncellendi!');
          setEditingRecord(null);
        } else {
          await axiosInstance.post('/calorie/create', newRecord);
          alert('Yeni kayıt başarıyla eklendi!');
        }
        setFormData({ recordDate: '', amount: '', target: '' });
        await fetchData();
      } catch (error) {
        console.error(error.response || error.message);
        alert('Kayıt eklenirken/güncellenirken hata oluştu!');
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
    if (window.confirm('Bu kaydı silmek istediğinize emin misiniz?')) {
      try {
        await axiosInstance.delete(`/calorie/delete/${id}`, {
          params: { userId: user.id },
        });
        alert('Kayıt başarıyla silindi!');
        await fetchData();
      } catch (error) {
        console.error(error.response || error.message);
        alert('Kayıt silinirken hata oluştu!');
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

  return (
    <div className="calorie-intake-container">
      <div className="header-section">
        <img src={caloriesIcon} alt="Calories Icon" className="calories-icon" />
      </div>

      {summary && (
        <div className="summary-section">
          <h3>Özet Bilgiler</h3>
          <p>Haftalık Toplam: {summary.weeklyTotal} kcal</p>
          <p>Aylık Toplam: {summary.monthlyTotal} kcal</p>
          <p>Yıllık Toplam: {summary.yearlyTotal} kcal</p>
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
            name="amount"
            value={formData.amount}
            onChange={handleInputChange}
            placeholder="Kalori (kcal)"
            required
          />
          <input
            type="number"
            name="target"
            value={formData.target}
            onChange={handleInputChange}
            placeholder="Hedef (kcal)"
            required
          />
          <button type="submit">{editingRecord ? 'Güncelle' : 'Ekle'}</button>
        </form>
      </div>

      <div className="chart-section">
        <CalorieChart data={data} />
      </div>

      <div className="slide-button-container">
        <button onClick={toggleTable}>
          {showTable ? 'Tabloyu Gizle' : 'Tabloyu Göster'}
        </button>
      </div>

      {showTable && (
        <div className={`table-section ${showTable ? 'show' : 'hide'}`}>
          <table>
            <thead>
              <tr>
                <th>Tarih</th>
                <th>Kalori (kcal)</th>
                <th>Hedef (kcal)</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              {currentRecords.map((record, index) => (
                <tr key={index}>
                  <td>{record.recordDate}</td>
                  <td>{record.amount} kcal</td>
                  <td>{record.target} kcal</td>
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
                className={currentPage === index + 1 ? 'active' : ''}
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

export default CalorieIntakePage;
