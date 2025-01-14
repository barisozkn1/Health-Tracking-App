import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { UserProvider } from './context/UserContext';
import Header from './components/Header/Header';
import AppRoutes from './routes/AppRoutes';

function App() {
  return (
    <UserProvider>
      <Router>
        <Header />
        <AppRoutes />
      </Router>
    </UserProvider>
  );
}

export default App;
