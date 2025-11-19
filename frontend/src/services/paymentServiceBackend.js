import api from '../utils/api';

  export const paymentServiceBackend = {
  createIntent: async (orderId, amount) => {
    try {
      const payload = { amount };
      console.log('Sending create-intent:', payload);

      // Interceptor returns response.data (or response.data.data)
      const data = await api.post(
        '/EventRegistration/API/User/Payment/create-intent',
        payload
      );

      console.log('create-intent response:', data);

      // Return the data object directly
      return data;

    } catch (error) {
      console.error('create-intent error:', error.response?.data || error.message);
      throw error;
    }
  },

  finalizeOrder: async (orderId, paymentIntentId) => {
    await api.post('/EventRegistration/API/User/Payment/finalize', { orderId, paymentIntentId });
  },
};