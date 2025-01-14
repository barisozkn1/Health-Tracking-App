import React, { useContext, useState, useEffect } from "react";
import { UserContext } from "../../context/UserContext";
import {
  getProfile,
  updateProfile,
  changePassword,
} from "../../services/profileService";
import "./ProfilePage.css";
import profileIcon from "../../assets/profil-img.png"; // İkon dosyasının doğru yolunu kontrol et

const ProfilePage = () => {
  const { user, logout } = useContext(UserContext); // logout fonksiyonunu ekledim
  const [profileData, setProfileData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [updateData, setUpdateData] = useState({
    userName: "",
    email: "",
    gender: "",
  });
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
  });

  // Profil bilgilerini yükle
  useEffect(() => {
    const fetchProfileData = async () => {
      if (user?.id) {
        try {
          const data = await getProfile(user.id);
          setProfileData(data);
          setUpdateData({
            userName: data.userName,
            email: data.email,
            gender: data.gender,
          });
        } catch (error) {
          console.error("Profil bilgileri alınırken hata oluştu:", error);
        } finally {
          setLoading(false);
        }
      }
    };
    fetchProfileData();
  }, [user]);

  // Profil güncelleme
  const handleProfileUpdate = async () => {
    try {
      const isEmailChanged = updateData.email !== profileData.email;
      await updateProfile(user.id, updateData);
      alert("Profil başarıyla güncellendi!");

      if (isEmailChanged) {
        alert("Email değişikliği yaptınız. Lütfen tekrar giriş yapınız.");
        logout(); // Logout işlemi
      }
    } catch (error) {
      console.error("Profil güncelleme hatası:", error);
      alert("Profil güncellenirken bir hata oluştu.");
    }
  };

  // Şifre değiştirme
  const handlePasswordChange = async () => {
    try {
      await changePassword(user.id, passwordData);
      alert("Şifre başarıyla değiştirildi! Lütfen tekrar giriş yapınız.");
      logout(); // Şifre değişikliği sonrası logout işlemi
    } catch (error) {
      console.error("Şifre değiştirme hatası:", error);
      alert("Şifre değiştirilemedi.");
    }
  };

  if (loading) {
    return <p>Yükleniyor...</p>;
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <h2>Profil</h2>
        <img src={profileIcon} alt="Profil Icon" className="profile-icon" />
      </div>

      {/* Kullanıcı Bilgileri */}
      <div className="profile-card">
        <h3>Kullanıcı Bilgileri</h3>
        <p>
          <strong>Ad Soyad:</strong> {profileData?.userName || "Bilinmiyor"}
        </p>
        <p>
          <strong>Cinsiyet:</strong> {profileData?.gender || "Bilinmiyor"}
        </p>
        <p>
          <strong>Email:</strong> {profileData?.email || "Bilinmiyor"}
        </p>
      </div>

      {/* Profil Güncelle */}
      <div className="profile-card">
        <h3>Profil Güncelle</h3>
        <input
          type="text"
          value={updateData.userName}
          onChange={(e) =>
            setUpdateData({ ...updateData, userName: e.target.value })
          }
          placeholder="Ad Soyad"
          disabled // Değiştirilmesini engellemek için
        />
        <input
          type="email"
          value={updateData.email}
          onChange={(e) =>
            setUpdateData({ ...updateData, email: e.target.value })
          }
          placeholder="Email"
        />
        <select
          value={updateData.gender}
          onChange={(e) =>
            setUpdateData({ ...updateData, gender: e.target.value })
          }
          disabled // Değiştirilmesini engellemek için
        >
          <option value="MALE">Erkek</option>
          <option value="FEMALE">Kadın</option>
        </select>
        <button onClick={handleProfileUpdate}>Güncelle</button>
      </div>

      {/* Şifre Değiştir */}
      <div className="profile-card">
        <h3>Şifre Değiştir</h3>
        <input
          type="password"
          value={passwordData.currentPassword}
          onChange={(e) =>
            setPasswordData({ ...passwordData, currentPassword: e.target.value })
          }
          placeholder="Mevcut Şifre"
        />
        <input
          type="password"
          value={passwordData.newPassword}
          onChange={(e) =>
            setPasswordData({ ...passwordData, newPassword: e.target.value })
          }
          placeholder="Yeni Şifre"
        />
        <button onClick={handlePasswordChange}>Şifreyi Güncelle</button>
      </div>
    </div>
  );
};

export default ProfilePage;
