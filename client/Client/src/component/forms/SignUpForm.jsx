// // File: src/components/forms/SignupForm.jsx
// import React, { useState } from 'react';
// import Input from '../common/Input';
// import RoleSelector from '../common/RoleSelector';
// import { Mail, Lock, User } from 'lucide-react';
// import { useAuth } from '../../context/AuthContext';
// import validation from '../../utils/validation';

// const SignupForm = () => {
//   const { signup, isLoading } = useAuth();
//   const [formData, setFormData] = useState({ name: '', email: '', password: '', role: '' });
//   const [errors, setErrors] = useState({});
//   const [showPassword, setShowPassword] = useState(false);

//   const handleChange = (key, value) => {
//     setFormData((prev) => ({ ...prev, [key]: value }));
//   };

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     const nameError = validation.validateName(formData.name);
//     const emailError = validation.validateEmail(formData.email);
//     const passwordError = validation.validatePassword(formData.password);
//     const roleError = validation.validateRole(formData.role);

//     if (nameError || emailError || passwordError || roleError) {
//       setErrors({ name: nameError, email: emailError, password: passwordError, role: roleError });
//       return;
//     }
//     setErrors({});
//     try {
//       await signup(formData);
//     } catch (err) {
//       alert(err.message);
//     }
//   };

//   return (
//     <form onSubmit={handleSubmit} className="space-y-6">
//       <Input
//         label="Name"
//         value={formData.name}
//         onChange={(e) => handleChange('name', e.target.value)}
//         error={errors.name}
//         placeholder="Enter your name"
//         icon={User}
//       />
//       <Input
//         label="Email"
//         type="email"
//         value={formData.email}
//         onChange={(e) => handleChange('email', e.target.value)}
//         error={errors.email}
//         placeholder="Enter your email"
//         icon={Mail}
//       />
//       <Input
//         label="Password"
//         value={formData.password}
//         onChange={(e) => handleChange('password', e.target.value)}
//         error={errors.password}
//         placeholder="Enter your password"
//         icon={Lock}
//         showPasswordToggle
//         showPassword={showPassword}
//         onTogglePassword={() => setShowPassword((prev) => !prev)}
//       />
//       <RoleSelector
//         value={formData.role}
//         onChange={(val) => handleChange('role', val)}
//         error={errors.role}
//       />
//       <button type="submit" disabled={isLoading} className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700 transition">
//         {isLoading ? 'Signing up...' : 'Sign Up'}
//       </button>
//     </form>
//   );
// };

// export default SignupForm;

// File: src/components/forms/SignupForm.jsx
// File: src/components/forms/SignupForm.jsx
import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import validation from '../../utils/validation';
import RoleSelector from '../common/RoleSelector'; // Retained to preserve original logic

const SignupForm = () => {
  const { signup, isLoading } = useAuth();
  const [formData, setFormData] = useState({ fullName: '', email: '', password: '', role: '' });
  const [errors, setErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (key, value) => {
    setFormData((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const nameError = validation.validateName(formData.fullName);
    const emailError = validation.validateEmail(formData.email);
    const passwordError = validation.validatePassword(formData.password);
    const roleError = validation.validateRole(formData.role);

    if (nameError || emailError || passwordError || roleError) {
      setErrors({ fullName: nameError, email: emailError, password: passwordError, role: roleError });
      return;
    }
    setErrors({});
    try {
      await signup(formData);
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center py-10 bg-gray-100">
      <form onSubmit={handleSubmit} className="bg-white text-gray-500 w-full max-w-[340px] mx-4 md:p-6 p-4 py-8 text-left text-sm rounded-xl shadow-[0px_0px_10px_0px] shadow-black/10">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Sign Up</h2>

        {/* Username Input Field */}
        <input 
          id="fullName" 
          className="w-full border mt-1 bg-indigo-500/5 mb-2 border-gray-500/10 outline-none rounded py-2.5 px-3" 
          type="text" 
          placeholder="Username" 
          value={formData.fullName}
          onChange={(e) => handleChange('fullName', e.target.value)}
          required
        />
        {errors.fullName && <p className="text-red-500 text-xs mt-1 ml-2">{errors.fullName}</p>}

        {/* Email Input Field */}
        <input 
          id="email" 
          className="w-full border mt-1 bg-indigo-500/5 mb-2 border-gray-500/10 outline-none rounded py-2.5 px-3" 
          type="email" 
          placeholder="Email" 
          value={formData.email}
          onChange={(e) => handleChange('email', e.target.value)}
          required
        />
        {errors.email && <p className="text-red-500 text-xs mt-1 ml-2">{errors.email}</p>}

        {/* Password Input Field */}
        <input 
          id="password" 
          className="w-full border mt-1 bg-indigo-500/5 mb-7 border-gray-500/10 outline-none rounded py-2.5 px-3" 
          type="password" 
          placeholder="Password" 
          value={formData.password}
          onChange={(e) => handleChange('password', e.target.value)}
          required
        />
        {errors.password && <p className="text-red-500 text-xs mt-1 ml-2">{errors.password}</p>}
        
        {/* Role Selector */}
        <RoleSelector
          value={formData.role}
          onChange={(val) => handleChange('role', val)}
          error={errors.role}
        />
        
        {/* Submit Button */}
        <button 
          type="submit" 
          disabled={isLoading} 
          className="w-full mb-3 bg-indigo-500 hover:bg-indigo-600/90 transition-all active:scale-95 py-2.5 rounded text-white font-medium disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isLoading ? 'Creating Account...' : 'Create Account'}
        </button>
        
        {/* Login Link */}
        <p className="text-center mt-4">Already have an account? <a href="/login" className="text-blue-500 underline">Log In</a></p>
      </form>
    </div>
  );
};

export default SignupForm;
