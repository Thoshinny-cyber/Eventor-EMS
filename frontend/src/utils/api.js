import axios from 'axios';

const API_BASE_URL = "/api";

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const auth = JSON.parse(localStorage.getItem('auth') || '{}');
    if (auth.username && auth.password) {
      config.auth = {
        username: auth.username,
        password: auth.password,
      };
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    // Backend returns APIResponse with { status, data, timestamp, error }
    // Extract the actual data from response.data.data
    if (response.data && response.data.data !== undefined) {
      return response.data.data;
    }
    return response.data;
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('auth');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;

