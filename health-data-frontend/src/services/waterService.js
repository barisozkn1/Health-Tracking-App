import axiosInstance from './axiosConfig';

// Kullanıcı ID doğrulama
const validateUserId = (userId) => {
  if (!userId) {
    console.error('User ID bulunamadı');
    throw new Error('User ID bulunamadı');
  }
};

// Hata yönetimi yardımcı fonksiyonu
const handleError = (error, message) => {
  console.error(message, error.response?.data || error.message);
  throw error;
};

// Su tüketimi verilerini getir
export const fetchWaterData = async (userId) => {
  validateUserId(userId);
  try {
    const response = await axiosInstance.get(`/rest/api/water/list/${userId}`);
    return response.data;
  } catch (error) {
    handleError(error, 'Fetch water data error:');
  }
};

// Su tüketimi verisi ekle
export const addWaterData = async (userId, data) => {
  validateUserId(userId);
  try {
    const response = await axiosInstance.post(`/rest/api/water/create`, { userId, ...data });
    return response.data;
  } catch (error) {
    handleError(error, 'Add water data error:');
  }
};

// Su tüketimi verisini güncelle
export const updateWaterData = async (userId, dataId, updatedData) => {
  validateUserId(userId);
  try {
    const response = await axiosInstance.put(`/rest/api/water/update/${dataId}`, { userId, ...updatedData });
    return response.data;
  } catch (error) {
    handleError(error, 'Update water data error:');
  }
};

// Su tüketimi verisini sil
export const deleteWaterData = async (userId, dataId) => {
  validateUserId(userId);
  try {
    await axiosInstance.delete(`/rest/api/water/delete/${dataId}`);
  } catch (error) {
    handleError(error, 'Delete water data error:');
  }
};
