// File: src/services/authService.js
import axios from 'axios';

axios.defaults.baseURL = import.meta.env.VITE_BASE_URL
axios.defaults.withCredentials = true;

const authService = {
  async signup(userData) {
    try {
      const response = await axios.post('/v0/auth/signup', userData);
      //console.log(response.data);
      return response.data;
    } catch (error) {
      throw new Error(`Signup failed: ${error.response.status}`);
    }
  },

  async login(credentials) {
    try {
      const response = await axios.post('/v0/auth/login', credentials);
      console.log("From authService ");
      console.log(response);
      return response.data;
    } catch (error) {
      throw new Error(`Login failed: ${error.response.status}`);
    }
  },

  async logout() {
    try {
      await axios.post('/api/auth/logout');
      return true;
    } catch (error) {
      console.error('Logout failed:', error);
      return false;
    }
  },
};

export default authService;