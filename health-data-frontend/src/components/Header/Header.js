import React, { useContext, useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './Header.css';
import logo from '../../assets/logo.png';
import { UserContext } from '../../context/UserContext';

const Header = () => {
  const { user, logout, verifySession } = useContext(UserContext);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const checkSession = async () => {
      if (!user) {
        try {
          await verifySession(); // Kullanıcı oturumunu kontrol et
        } catch (error) {
          console.error('Session verification failed:', error);
        }
      }
    };
    checkSession();
  }, [user, verifySession]);

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login'); // Çıkış yaptıktan sonra login sayfasına yönlendir
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const handleHomeClick = () => {
    navigate(user ? '/dashboard' : '/'); // Kullanıcı oturum açmışsa dashboard'a, aksi halde anasayfaya yönlendir
  };

  return (
    <header className="header">
      <div className="logo" onClick={handleHomeClick}>
        <img src={logo} alt="Logo" className="logo-image" />
      </div>
      <nav className="nav">
        <ul>
          <li>
            <span className="nav-link" onClick={handleHomeClick}>
              Anasayfa
            </span>
          </li>
          <li>
            <Link to="/about" className="nav-link">
              Hakkımızda
            </Link>
          </li>
          <li>
            <Link to="/contact" className="nav-link">
              İletişim
            </Link>
          </li>
          {user ? (
            <div
              className="user-dropdown"
              onClick={() => setDropdownOpen(!dropdownOpen)}
            >
              <span className="user-name">{user.userName}</span>
              {dropdownOpen && (
                <div className="dropdown-menu">
                  <Link to="/profile">Profil</Link>
                  <button onClick={handleLogout}>Çıkış</button>
                </div>
              )}
            </div>
          ) : (
            <li>
              <Link to="/login" className="login-button">
                Giriş
              </Link>
            </li>
          )}
        </ul>
      </nav>
    </header>
  );
};

export default Header;
