import React, { useContext } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Home from '../pages/Home/Home';
import Login from '../pages/Login/Login';
import Register from '../pages/Register/Register';
import About from '../pages/About/About'; // Hakkımızda sayfası
import Contact from '../pages/Contact/Contact';
import ResetPassword from '../pages/Password/ResetPassword';
import Dashboard from '../pages/Dashboard/Dashboard';
import WaterIntakePage from '../pages/WaterIntake/WaterIntakePage'; // Su Tüketimi Sayfası
import CalorieIntakePage from '../pages/CalorieIntake/CalorieIntakePage'; // Kalori Modülü
import StepCounterPage from '../pages/StepCounter/StepCounterPage'; // Adım Sayacı Modülü
import { UserContext } from '../context/UserContext';
import HeartRatePage from '../pages/HeartRate/HeartRatePage'; // Kalp Atış Hızı  Modülü
import HeightWeightPage from '../pages/HeightWeight/HeightWeightPage'; //Boy-Kilo Modülü
import SleepPage from '../pages/Sleep/SleepPage'; //Sleep Modülü
import ProfilePage from '../pages/Profile/ProfilePage';

const AppRoutes = () => {
  const { user } = useContext(UserContext);

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route
        path="/login"
        element={!user ? <Login /> : <Navigate to="/dashboard" replace />}
      />
      <Route path="/register" element={<Register />} />
      <Route path="/about" element={<About />} /> {/* Hakkımızda */}
      <Route path="/contact" element={<Contact />} />
      <Route path="/reset-password" element={<ResetPassword />} />
      <Route
        path="/dashboard"
        element={user ? <Dashboard /> : <Navigate to="/login" replace />}
      /> {/* Korunan rota */}
      <Route
        path="/water"
        element={user ? <WaterIntakePage /> : <Navigate to="/login" replace />}
      /> {/* Su Tüketimi Modülü */}
      <Route
        path="/calorie"
        element={user ? <CalorieIntakePage /> : <Navigate to="/login" replace />}
      /> {/* Kalori Modülü */}
      <Route
         path="/steps"
         element={user ? <StepCounterPage /> : <Navigate to="/login" replace />}
      /> {/* Adım Sayacı Modülü */}
      <Route
         path="/heart-rate"
         element={user ? <HeartRatePage /> : <Navigate to="/login" replace />}
      /> {/* Kalp Atış Hızı Modülü */}
        <Route
         path="/height-weight"
         element={user ? <HeightWeightPage /> : <Navigate to="/login" replace />}
      /> {/* Boy-Kilo Modülü */}
        <Route
         path="/sleep"
         element={user ? <SleepPage /> : <Navigate to="/login" replace />}
      /> {/* Sleep Modülü */}
        <Route
        path="/profile"
        element={user ? <ProfilePage /> : <Navigate to="/login" replace />}
        />{/* Profile Kısmı */}
    </Routes>
  );
};

export default AppRoutes;
