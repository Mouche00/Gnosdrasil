import React from 'react';

interface ButtonProps {
  children: React.ReactNode;
  onClick?: () => void;
  type?: 'button' | 'submit' | 'reset';
  className?: string;
  disabled?: boolean;
}

const Button: React.FC<ButtonProps> = ({ children, onClick, type = 'button', className = '', disabled = false }) => {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`px-6 py-3 rounded-xl bg-gray-100 shadow-lg hover:shadow-xl 
                 active:shadow-inner active:bg-gray-200 transition-all duration-200
                 text-gray-700 font-medium disabled:opacity-50 disabled:cursor-not-allowed
                 ${className}`}
    >
      {children}
    </button>
  );
};

export default Button; 