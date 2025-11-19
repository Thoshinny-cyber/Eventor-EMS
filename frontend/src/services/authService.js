import api from '../utils/api';

export const authService = {
  login: async (username, password) => {
    const response = await api.post('/api/auth/login', { username, password });
    if (response) {
      // Store credentials for Basic Auth
      localStorage.setItem('auth', JSON.stringify({ username, password, ...response }));
      return response;
    }
    throw new Error('Login failed');
  },

  register: async (userData) => {
    try {
      const response = await api.post('/api/auth/register', userData);
      if (response) {
        return response;
      }
      throw new Error('Registration failed');
    } catch (error) {
      console.error('Registration error:', error);
      if (error.response) {
        // Server responded with error
        throw new Error(error.response.data?.message || `Server error: ${error.response.status}`);
      } else if (error.request) {
        // Request made but no response (backend not running)
        throw new Error('Cannot connect to server. Please ensure the backend is running on http://localhost:8080');
      } else {
        // Something else happened
        throw new Error('Registration failed: ' + error.message);
      }
    }
  },

  logout: () => {
    localStorage.removeItem('auth');
  },

  getCurrentUser: () => {
    const auth = localStorage.getItem('auth');
    return auth ? JSON.parse(auth) : null;
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('auth');
  },
};

