// File: src/components/common/RoleSelector.jsx
import React from 'react';
import { GraduationCap, Users } from 'lucide-react';

const roles = [
  { id: 'FACULTY', label: 'Teacher', icon: GraduationCap, description: 'Manage classes and student progress' },
  { id: 'PARENTS', label: 'Parent', icon: Users, description: 'Monitor your child\'s education' }
];

const RoleSelector = ({ value, onChange, error }) => (
  <div className="space-y-2">
    <label className="text-sm font-medium text-gray-700 block">Select Role</label>
    <div className="grid grid-cols-1 gap-3">
      {roles.map((role) => (
        <button key={role.id} type="button" onClick={() => onChange(role.id)} className={`p-4 border-2 rounded-lg text-left transition-all ${value === role.id ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-gray-300'}`}>
          <div className="flex items-center space-x-3">
            <role.icon className={`w-6 h-6 ${value === role.id ? 'text-blue-600' : 'text-gray-400'}`} />
            <div>
              <div className={`font-medium ${value === role.id ? 'text-blue-900' : 'text-gray-900'}`}>{role.label}</div>
              <div className={`text-sm ${value === role.id ? 'text-blue-600' : 'text-gray-500'}`}>{role.description}</div>
            </div>
          </div>
        </button>
      ))}
    </div>
    {error && <p className="text-red-600 text-sm">{error}</p>}
  </div>
);

export default RoleSelector;