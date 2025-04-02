import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Home: React.FC = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="min-h-[calc(100vh-6rem)] flex flex-col items-center justify-center">
      <div className="text-center space-y-8">
        <h1 className="text-5xl md:text-6xl font-bold bg-gradient-to-r from-pastel-blue via-pastel-purple to-pastel-pink bg-clip-text text-transparent">
          Welcome to Gnosdrasil
        </h1>
        <p className="text-xl text-soft-dark max-w-2xl mx-auto">
          Your AI-powered creative companion for generating unique and engaging content.
        </p>
        
        <div className="flex flex-col sm:flex-row gap-4 justify-center mt-8">
          {!isAuthenticated ? (
            <>
              <Link
                to="/register"
                className="px-8 py-4 rounded-2xl bg-gradient-to-r from-pastel-pink to-pastel-purple text-white text-lg font-medium shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300"
              >
                Get Started
              </Link>
              <Link
                to="/login"
                className="px-8 py-4 rounded-2xl bg-soft-gray text-soft-dark text-lg font-medium shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300"
              >
                Sign In
              </Link>
            </>
          ) : (
            <Link
              to="/prompt"
              className="px-8 py-4 rounded-2xl bg-gradient-to-r from-pastel-blue to-pastel-purple text-white text-lg font-medium shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300"
            >
              Start Creating
            </Link>
          )}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-16">
          <div className="p-6 rounded-2xl bg-soft-gray shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-pastel-blue to-pastel-purple flex items-center justify-center mb-4">
              <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold text-soft-dark mb-2">Lightning Fast</h3>
            <p className="text-gray-600">Generate content in seconds with our advanced AI technology</p>
          </div>

          <div className="p-6 rounded-2xl bg-soft-gray shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-pastel-pink to-pastel-purple flex items-center justify-center mb-4">
              <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 5a1 1 0 011-1h14a1 1 0 011 1v2a1 1 0 01-1 1H5a1 1 0 01-1-1V5zM4 13a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H5a1 1 0 01-1-1v-6zM16 13a1 1 0 011-1h2a1 1 0 011 1v6a1 1 0 01-1 1h-2a1 1 0 01-1-1v-6z" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold text-soft-dark mb-2">Creative Freedom</h3>
            <p className="text-gray-600">Unleash your creativity with our versatile content generation tools</p>
          </div>

          <div className="p-6 rounded-2xl bg-soft-gray shadow-neumorphic-sm hover:shadow-neumorphic transition-all duration-300">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-pastel-green to-pastel-blue flex items-center justify-center mb-4">
              <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold text-soft-dark mb-2">Secure & Reliable</h3>
            <p className="text-gray-600">Your content is safe with our enterprise-grade security</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home; 