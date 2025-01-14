import axiosInstance from "./axiosConfig";
// Kullanıcı bilgilerini getir
export const getProfile = async (userId) => {
  try {
    const response = await axiosInstance.get(`/profile/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Profil bilgileri alınamadı:", error);
    throw error;
  }
};

// Profil güncelle
export const updateProfile = async (userId, updatedData) => {
  try {
    const response = await axiosInstance.put(`/profile/${userId}`, updatedData);
    return response.data;
  } catch (error) {
    console.error("Profil güncellenemedi:", error);
    throw error;
  }
};

// Şifre değiştir
export const changePassword = async (userId, passwordData) => {
  try {
    const response = await axiosInstance.patch(`/profile/password/${userId}`, passwordData);
    return response.data;
  } catch (error) {
    console.error("Şifre değiştirilemedi:", error);
    throw error;
  }
};
