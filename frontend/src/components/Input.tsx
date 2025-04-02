import React from 'react';

interface InputProps {
  type?: string;
  name: string;
  placeholder?: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  className?: string;
  label?: string;
  required?: boolean;
}

const Input: React.FC<InputProps> = ({
  type = 'text',
  name,
  placeholder,
  value,
  onChange,
  className = '',
  label,
  required = false
}) => {
  return (
    <div className="w-full">
      {label && (
        <label className="block text-gray-700 mb-2 font-medium">
          {label}
        </label>
      )}
      <input
        type={type}
        name={name}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        required={required}
        className={`w-full px-4 py-3 rounded-xl bg-gray-100 shadow-inner
                   focus:outline-none focus:ring-2 focus:ring-gray-300
                   text-gray-700 placeholder-gray-400 ${className}`}
      />
    </div>
  );
};

export default Input; 