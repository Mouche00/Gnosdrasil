import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar: React.FC = () => {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login');
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  const isActive = (path: string) => location.pathname === path;

  return (
    <nav className={`fixed w-full z-50 transition-all duration-300 ${
      scrolled ? 'bg-soft-gray/80 backdrop-blur-md shadow-neumorphic-sm' : 'bg-transparent'
    }`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-20">
          <div className="flex items-center">
            <Link 
              to="/" 
              className="text-3xl font-bold bg-gradient-to-r from-pastel-blue to-pastel-purple bg-clip-text text-transparent hover:from-pastel-purple hover:to-pastel-blue transition-all duration-300"
            >
              Gnosdrasil
            </Link>
          </div>

          <div className="flex items-center space-x-6">
            {isAuthenticated ? (
              <div className="relative">
                <button
                  onClick={() => setIsMenuOpen(!isMenuOpen)}
                  className="flex items-center space-x-3 px-4 py-2 rounded-full bg-soft-gray shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300"
                >
                  <div className="w-8 h-8 rounded-full bg-gradient-to-br from-pastel-blue to-pastel-purple flex items-center justify-center text-white font-medium">
                    {user?.username[0].toUpperCase()}
                  </div>
                  <span className="text-lg font-medium text-soft-dark">{user?.username}</span>
                  <svg
                    className={`w-5 h-5 transform transition-transform duration-300 ${isMenuOpen ? 'rotate-180' : ''}`}
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                  </svg>
                </button>

                {isMenuOpen && (
                  <div className="absolute right-0 mt-3 w-56 bg-soft-gray rounded-2xl shadow-neumorphic z-50">
                    <button
                      onClick={handleLogout}
                      className="w-full text-left px-4 py-3 text-soft-dark hover:bg-white/50 transition-colors duration-200 flex items-center space-x-3"
                    >
                      <svg className="w-5 h-5 text-pastel-pink" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                      </svg>
                      <span>Logout</span>
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <div className="flex space-x-4">
                <Link
                  to="/login"
                  className={`px-6 py-2 rounded-full transition-all duration-300 ${
                    isActive('/login')
                      ? 'bg-gradient-to-r from-pastel-blue to-pastel-purple text-white shadow-neumorphic-sm'
                      : 'text-soft-dark hover:text-pastel-blue'
                  }`}
                >
                  Login
                </Link>
                <Link
                  to="/register"
                  className="px-6 py-2 rounded-full bg-gradient-to-r from-pastel-pink to-pastel-purple text-white shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300"
                >
                  Register
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar; 