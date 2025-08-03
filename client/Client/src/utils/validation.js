// File: src/utils/validation.js
const validation = {
  validateEmail: (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email) return 'Email is required';
    if (!emailRegex.test(email)) return 'Please enter a valid email';
    return null;
  },
  validatePassword: (password) => {
    if (!password) return 'Password is required';
    if (password.length < 6) return 'Password must be at least 6 characters';
    return null;
  },
  validateName: (name) => {
    if (!name || name.trim().length === 0) return 'Name is required';
    if (name.trim().length < 2) return 'Name must be at least 2 characters';
    return null;
  },
  validateRole: (role) => {
    if (!role) return 'Please select a role';
    if (!['FACULTY', 'PARENTS'].includes(role)) return 'Invalid role selected';
    return null;
  },
};

export default validation;