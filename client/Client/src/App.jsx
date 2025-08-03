import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import LoginForm from './component/forms/LoginForm';
import SignupForm from './component/forms/SignUpForm';
import TeacherDashboard from './component/dashboards/TeacherDashboard';
import ParentDashboard from './component/dashboards/ParentDashboard';
import Layout from './component/Layout';
import AdminDashboard from './component/dashboards/Admin';


const DashboardRedirect = () => {
  const { user } = useAuth();

  if (!user) return <Navigate to="/signup" />;
  if (user.role === 'FACULTY') return <TeacherDashboard />;
  if (user.role === 'PARENTS') return <ParentDashboard />;
  if (user.role === 'ADMIN') return <AdminDashboard/>;
  return <div>Unknown role</div>;
};

const App = () => {
  
  
  return (
    <AuthProvider>
      <Router>
       
        <div className="min-h-screen bg-gray-50">
          <Routes>
            {/* {user === null && <LoginForm/>} */}
            <Route path="/" element={<Layout/>} />
            <Route path="/dashboard" element={<DashboardRedirect />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/signup" element={<SignupForm />} />
            {/* <Route path="*" element={<Navigate to="/" />} /> */}
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
};

export default App;

