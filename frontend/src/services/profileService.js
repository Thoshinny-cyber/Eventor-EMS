import api from '../utils/api';

export const profileService = {
  getUserProfile: async (userId) => {
    try {
      const response = await api.get(`/EventRegistration/API/User/profile/${userId}`);
      console.log('Raw API response in service:', response);

      // Interceptor returns response.data.data → already unwrapped
      if (!response) {
        throw new Error('No data returned from API');
      }

      console.log('Unwrapped profile data:', response);
      return response; // ← return the object directly
    } catch (error) {
      console.error('Error fetching profile:', error);
      throw error;
    }
  },

  updateProfile: async (profileData) => {
    try {
      const response = await api.put('/EventRegistration/API/User/profile', profileData);
      console.log('Profile update response:', response);
      if (!response || !response.data) {
        throw new Error('Failed to update profile');
      }
      return response.data;
    } catch (error) {
      console.error('Error updating profile:', error);
      throw error;
    }
  },
};
