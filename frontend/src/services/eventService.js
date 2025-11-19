import api from '../utils/api';

export const eventService = {
  getAllEvents: async () => {
    try {
      const response = await api.get('/EventRegistration/API/User/Event');
      console.log('getAllEvents response:', response);
      if (Array.isArray(response)) {
        console.log('Events (direct array):', response);
        return response;
      }
      if (!response.data) {
        console.error('No data in response:', response);
        return [];
      }
      if (!response.data.data) {
        console.error('No events in response.data:', response.data);
        return [];
      }
      console.log('Events:', response.data.data);
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching events:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
      });
      return [];
    }
  },

  getTopEvents: async () => {
    try {
      const response = await api.get('/EventRegistration/API/User/Event/Top');
      console.log('getTopEvents response:', response);
      if (Array.isArray(response)) {
        console.log('Top events (direct array):', response);
        return response;
      }
      if (!response.data) {
        console.error('No data in response:', response);
        return [];
      }
      if (!response.data.data) {
        console.error('No events in response.data:', response.data);
        return [];
      }
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching top events:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
      });
      return [];
    }
  },

  getEventById: async (id) => {
    try {
      const response = await api.get(`/EventRegistration/API/User/Event/${id}`);
      console.log('getEventById response:', response);
      // Check if response is an event object (direct response)
      if (response && typeof response === 'object' && response.id) {
        console.log('Event (direct object):', response);
        return response;
      }
      // Check for APIResponse structure
      if (!response.data) {
        console.error('No data in response:', response);
        return null;
      }
      if (!response.data.data) {
        console.error('No event data in response.data:', response.data);
        return null;
      }
      console.log('Event:', response.data.data);
      return response.data.data || null;
    } catch (error) {
      console.error(`Error fetching event ${id}:`, {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
      });
      return null;
    }
  },

  getFilteredEvents: async (filters) => {
    try {
      const response = await api.post('/EventRegistration/API/User/Event/filteredEvents', filters);
      console.log('getFilteredEvents response:', response);
      if (Array.isArray(response)) {
        console.log('Filtered events (direct array):', response);
        return response;
      }
      if (!response.data) {
        console.error('No data in response:', response);
        return [];
      }
      if (!response.data.data) {
        console.error('No events in response.data:', response.data);
        return [];
      }
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching filtered events:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
      });
      return [];
    }
  },
};