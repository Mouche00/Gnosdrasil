import React from 'react';
import { Link } from 'react-router-dom';
import Button from '../components/Button';

const Home: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-100 flex flex-col items-center justify-center p-4">
      <div className="max-w-4xl w-full bg-white rounded-2xl shadow-xl p-8 text-center">
        <h1 className="text-4xl font-bold text-gray-800 mb-6">Welcome to Gnosdrasil</h1>
        <p className="text-gray-600 mb-8">
          Your AI-powered platform for intelligent interactions and creative solutions.
        </p>
        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <Link to="/login">
            <Button>Login</Button>
          </Link>
          <Link to="/register">
            <Button className="bg-blue-500 text-white hover:bg-blue-600">
              Register
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Home; 