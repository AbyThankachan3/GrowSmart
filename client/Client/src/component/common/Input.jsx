// File: src/components/common/Input.jsx
import React from 'react';
import { Eye, EyeOff } from 'lucide-react';

const Input = ({ label, type = 'text', value, onChange, error, placeholder, icon: Icon, showPasswordToggle = false, onTogglePassword, showPassword = false }) => (
  <div className="space-y-2">
    <label className="text-sm font-medium text-gray-700 block">{label}</label>
    <div className="relative">
      {Icon && <Icon className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />}
      <input
        type={showPasswordToggle ? (showPassword ? 'text' : 'password') : type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className={`w-full ${Icon ? 'pl-10' : 'pl-4'} ${showPasswordToggle ? 'pr-10' : 'pr-4'} py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors ${error ? 'border-red-300 bg-red-50' : 'border-gray-300'}`}
      />
      {showPasswordToggle && (
        <button type="button" onClick={onTogglePassword} className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600">
          {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
        </button>
      )}
    </div>
    {error && <p className="text-red-600 text-sm">{error}</p>}
  </div>
);

export default Input;