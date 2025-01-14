import React, { useEffect, useState, useContext } from 'react';
import axiosInstance from '../../services/axiosConfig';
import HeartRateChart from '../../components/HeartRateChart/HeartRateChart';
import './HeartRatePage.css';
import { UserContext } from '../../context/UserContext';
import pulseIcon from '../../assets/pulse.png';

const HeartRatePage = () => {
  const { user } = useContext(UserContext);
  const [heartRateData, setHeartRateData] = useState([]);
  const [summary, setSummary] = useState(null);
  const [formData, setFormData] = useState({ recordDate: '', heartRate: '' });
  const [editingRecord, setEditingRecord] = useState(null);
  const [showTable, setShowTable] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const recordsPerPage = 5;

  useEffect(() => {
    if (user && user.id) {
      fetchHeartRateData();
      fetchSummaryData();
    }
  }, [user]);

  const fetchHeartRateData = async () => {
    try {
      const response = await axiosInstance.get(`/heart-rate/list/${user.id}`);
      setHeartRateData(response.data);
    } catch (error) {
      console.error('Kalp atış verileri alınamadı:', error);
    }
  };

  const fetchSummaryData = async () => {
    try {
      const response = await axiosInstance.get(`/heart-rate/summary/${user.id}?age=30`);
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
        await axiosInstance.put(`/heart-rate/update/${editingRecord.id}`, newRecord);
        setEditingRecord(null);
      } else {
        await axiosInstance.post('/heart-rate/create', newRecord);
      }
      setFormData({ recordDate: '', heartRate: '' });
      fetchHeartRateData();
      fetchSummaryData();
    } catch (error) {
      console.error('Kalp atış kaydı işlenirken hata oluştu:', error);
    }
  };

  const handleEdit = (record) => {
    setEditingRecord(record);
    setFormData({
      recordDate: record.recordDate,
      heartRate: record.heartRate,
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bu kaydı silmek istediğinize emin misiniz?')) {
      try {
        await axiosInstance.delete(`/heart-rate/delete/${id}`, {
          params: { userId: user.id },
        });
        fetchHeartRateData();
        fetchSummaryData();
      } catch (error) {
        console.error('Kalp atış kaydı silinirken hata oluştu:', error);
      }
    }
  };

  const toggleTable = () => {
    setShowTable((prev) => !prev);
  };

  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = heartRateData.slice(indexOfFirstRecord, indexOfLastRecord);
  const totalPages = Math.ceil(heartRateData.length / recordsPerPage);

  const barChartData = {
    labels: heartRateData.map((data) => data.recordDate),
    datasets: [
      {
        label: 'Kalp Atış Hızı (BPM)',
        data: heartRateData.map((data) => data.heartRate),
        backgroundColor: '#f44336',
      },
    ],
  };

  return (
    <div className="heart-rate-container">
      <div className="header-section">
        <img src={pulseIcon} alt="Pulse Icon" className="pulse-icon" />
      </div>

      {summary && (
        <div className="summary-section">
          <h3>Özet Bilgiler</h3>
          <p>Ortalama Kalp Atış Hızı: {summary.averageHeartRate} BPM</p>
          <p>HRV: {summary.hrv.toFixed(2)}</p>
          <p>Egzersiz Analizi: {summary.exerciseAnalysis}</p>
        </div>
      )}

      <HeartRateChart barChartData={barChartData} />

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
            name="heartRate"
            value={formData.heartRate}
            onChange={handleInputChange}
            placeholder="Kalp Atış Hızı (BPM)"
            required
          />
          <button type="submit">{editingRecord ? 'Güncelle' : 'Ekle'}</button>
        </form>
      </div>

      <div className="toggle-table-container">
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
                <th>Kalp Atış Hızı (BPM)</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              {currentRecords.map((record, index) => (
                <tr key={index}>
                  <td>{record.recordDate}</td>
                  <td>{record.heartRate} BPM</td>
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

export default HeartRatePage;
