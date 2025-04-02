import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import PublicRoute from './components/PublicRoute';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Prompt from './pages/Prompt';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="min-h-screen bg-gradient-to-br from-pastel-blue/20 via-pastel-purple/20 to-pastel-pink/20 relative">
          <div className="absolute inset-0 bg-[url('/grid.svg')] opacity-10 pointer-events-none"></div>
          <div className="relative z-10">
            <Navbar />
            <main className="container mx-auto px-4 py-8 pt-24">
              <div className="max-w-5xl mx-auto">
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route
                    path="/login"
                    element={
                      <PublicRoute>
                        <Login />
                      </PublicRoute>
                    }
                  />
                  <Route
                    path="/register"
                    element={
                      <PublicRoute>
                        <Register />
                      </PublicRoute>
                    }
                  />
                  <Route
                    path="/prompt"
                    element={
                      <ProtectedRoute>
                        <Prompt />
                      </ProtectedRoute>
                    }
                  />
                </Routes>
              </div>
            </main>
          </div>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
