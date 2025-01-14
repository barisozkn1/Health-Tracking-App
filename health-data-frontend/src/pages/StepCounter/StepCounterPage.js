import React, { useEffect, useState, useContext } from 'react';
import axiosInstance from '../../services/axiosConfig';
import StepChart from '../../components/StepChart/StepChart';
import './StepCounterPage.css';
import { UserContext } from '../../context/UserContext';
import footprintIcon from '../../assets/footprint.png';

const StepCounterPage = () => {
  const { user } = useContext(UserContext);
  const [stepData, setStepData] = useState([]);
  const [summary, setSummary] = useState(null);
  const [formData, setFormData] = useState({ recordDate: '', stepCount: '', target: '' });
  const [editingRecord, setEditingRecord] = useState(null);
  const [showTable, setShowTable] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const recordsPerPage = 5;

  useEffect(() => {
    if (user && user.id) {
      fetchStepData();
      fetchSummaryData();
    }
  }, [user]);

  const fetchStepData = async () => {
    try {
      const response = await axiosInstance.get(`/step/list/${user.id}`);
      setStepData(response.data);
    } catch (error) {
      console.error('Adım verileri alınamadı:', error);
    }
  };

  const fetchSummaryData = async () => {
    try {
      const response = await axiosInstance.get(`/step/summary/${user.id}`);
      setSummary(response.data);
    } catch (error) {
      console.error('Özet verisi alınamadı:', error);
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
        await axiosInstance.put(`/step/update/${editingRecord.id}`, newRecord);
        setEditingRecord(null);
      } else {
        await axiosInstance.post('/step/create', newRecord);
      }
      setFormData({ recordDate: '', stepCount: '', target: '' });
      fetchStepData();
      fetchSummaryData();
    } catch (error) {
      console.error('Adım kaydı işlenirken hata oluştu:', error);
    }
  };

  const handleEdit = (record) => {
    setEditingRecord(record);
    setFormData({
      recordDate: record.recordDate,
      stepCount: record.stepCount,
      target: record.target,
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bu kaydı silmek istediğinize emin misiniz?')) {
      try {
        await axiosInstance.delete(`/step/delete/${id}`, {
          params: { userId: user.id },
        });
        fetchStepData();
        fetchSummaryData();
      } catch (error) {
        console.error('Adım kaydı silinirken hata oluştu:', error);
      }
    }
  };

  const toggleTable = () => {
    setShowTable((prev) => !prev);
  };

  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = stepData.slice(indexOfFirstRecord, indexOfLastRecord);
  const totalPages = Math.ceil(stepData.length / recordsPerPage);

  const barChartData = {
    labels: stepData.map((data) => data.recordDate),
    datasets: [
      {
        label: 'Adım Sayısı',
        data: stepData.map((data) => data.stepCount),
        backgroundColor: '#4caf50',
      },
    ],
  };

  const pieChartData = {
    labels: ['Gerçekleşen', 'Eksik'],
    datasets: [
      {
        data: summary
          ? [summary.goalAchievementRate, 100 - summary.goalAchievementRate]
          : [0, 0],
        backgroundColor: ['#4caf50', '#f44336'],
      },
    ],
  };

  return (
    <div className="step-counter-container">
      <div className="header-section">
        <img src={footprintIcon} alt="Footprint Icon" className="footprint-icon" />
      </div>

      {summary && (
        <div className="summary-section">
          <h3>Özet Bilgiler</h3>
          <p>Haftalık Toplam: {summary.weeklyTotal} adım</p>
          <p>Aylık Toplam: {summary.monthlyTotal} adım</p>
          <p>Yıllık Toplam: {summary.yearlyTotal} adım</p>
          <p>Hedefe Ulaşma Oranı: %{summary.goalAchievementRate.toFixed(2)}</p>
        </div>
      )}

      <div className={`form-section ${showTable ? 'hide' : 'show'}`}>
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
            name="stepCount"
            value={formData.stepCount}
            onChange={handleInputChange}
            placeholder="Adım Sayısı"
            required
          />
          <input
            type="number"
            name="target"
            value={formData.target}
            onChange={handleInputChange}
            placeholder="Hedef (adım)"
            required
          />
          <button type="submit">{editingRecord ? 'Güncelle' : 'Ekle'}</button>
        </form>
      </div>

      <StepChart barChartData={barChartData} pieChartData={pieChartData} />

      <div className="toggle-table-container">
        <button className="toggle-table-btn" onClick={toggleTable}>
          {showTable ? 'Tabloyu Gizle' : 'Tabloyu Göster'}
        </button>
      </div>

      {showTable && (
        <div className="table-section">
          <table>
            <thead>
              <tr>
                <th>Tarih</th>
                <th>Adım Sayısı</th>
                <th>Hedef</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              {currentRecords.map((record, index) => (
                <tr key={index}>
                  <td>{record.recordDate}</td>
                  <td>{record.stepCount}</td>
                  <td>{record.target}</td>
                  <td>
                    <button className="edit-btn" onClick={() => handleEdit(record)}>
                      Düzenle
                    </button>
                    <button
                      className="delete-btn"
                      onClick={() => handleDelete(record.id)}
                    >
                      Sil
                    </button>
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

export default StepCounterPage;
