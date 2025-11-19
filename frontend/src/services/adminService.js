import api from '../utils/api';

export const adminService = {
  getAllEvents: async () => {
    try {
      const response = await api.get('/EventRegistration/API/Admin/Event/all');
      console.log('adminService: Raw API response:', response);

      // CASE 1: Interceptor returns response.data.data
      if (response?.data?.data && Array.isArray(response.data.data)) {
        console.log('adminService: Final events (from data.data):', response.data.data);
        return response.data.data;
      }

      // CASE 2: Interceptor returns response.data (array)
      if (response?.data && Array.isArray(response.data)) {
        console.log('adminService: Final events (from data):', response.data);
        return response.data;
      }

      // CASE 3: Interceptor returns array directly
      if (Array.isArray(response)) {
        console.log('adminService: Final events (direct array):', response);
        return response;
      }

      // CASE 4: Fallback
      console.warn('adminService: Unexpected format, returning []');
      return [];
    } catch (error) {
      console.error('adminService.getAllEvents error:', error);
      return [];
    }
  },

  getEventById: async (id) => {
    const response = await api.get(`/EventRegistration/API/Admin/Event/${id}`);
    return response;
  },

  createEvent: async (formData) => {
    const response = await api.post(
      '/EventRegistration/API/Admin/Event',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );
    return response || [];
  },

  updateEvent: async (eventData) => {
  console.log('Sending update:', eventData);
  const response = await api.put('/EventRegistration/API/Admin/Event', eventData);
  console.log('Update response:', response);
  return response;
},

  deleteEvent: async (id) => {
    const response = await api.delete(`/EventRegistration/API/Admin/Event/delete/${id}`);
    return response || [];
  },
};

