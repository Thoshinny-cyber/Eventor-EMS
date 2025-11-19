import api from '../utils/api';


export const categoryService = {
  getAllCategories: async () => {
    try {
      const response = await api.get('/EventRegistration/API/User/Category/all');
      console.log('Full API response:', response);

      // Check if response is an array (direct category list)
      if (Array.isArray(response)) {
        console.log('Categories (direct array):', response);
        return response;
      }

      // Check for APIResponse structure
      if (!response.data) {
        console.error('No data in response:', response);
        return [];
      }
      if (!response.data.data) {
        console.error('No categories in response.data:', response.data);
        return [];
      }
      console.log('Categories:', response.data.data);
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching categories:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
      });
      return [];
    }
  },

  getCategoryById: async (id) => {
    try {
      const response = await api.get(`/EventRegistration/API/User/Category/${id}`);
      console.log('Category by ID response:', response);
      if (!response.data || !response.data.data) {
        console.error('No category data in response:', response.data);
        return {};
      }
      return response.data.data || {};
    } catch (error) {
      console.error(`Error fetching category ${id}:`, error);
      return {};
    }
  },

  showCategory: async (id) => {
    try {
      const response = await api.get(`/EventRegistration/API/User/Category/show/${id}`);
      console.log('Show category response:', response);
      if (!response.data || !response.data.data) {
        console.error('No category data in response:', response.data);
        return {};
      }
      return response.data.data || {};
    } catch (error) {
      console.error(`Error showing category ${id}:`, error);
      return {};
    }
  },
};