import api from '../utils/api';

export const orderService = {
  getUserOrders: async (userId) => {
    try {
      const response = await api.get(`/EventRegistration/API/User/Order/${userId}`);
      console.log('getUserOrders raw response:', response); // DEBUG
      return response || []; // ← interceptor returns response.data.data
    } catch (error) {
      console.error('getUserOrders error:', error);
      return [];
    }
  },

bookTickets: async (orderData) => {
    try {
      // Build bookedSeats correctly
      const bookedSeats = Array.isArray(orderData.bookedSeats)
        ? orderData.bookedSeats.map(seat => ({
            seatNumber: seat,
            isSeatBooked: true,
            // DO NOT send userid, eventid, orderid — backend sets them
          }))
        : [];

      // FINAL PAYLOAD — matches OrderRequest exactly
      const payload = {
        userId: orderData.userId,
        eventId: orderData.eventId,
        count: orderData.count,
        totalPrice: orderData.totalPrice,
        paymentStatus: 'PAID', // ← MUST SEND
        bookedSeats,
      };

      console.log('bookTickets payload:', payload);

      const response = await api.post(
        '/EventRegistration/API/User/Order/Ticketbooking',
        payload
      );

      // Interceptor returns response.data.data
      const data = response?.data || response;
      console.log('bookTickets success:', data);
      return data;

    } catch (error) {
      console.error('bookTickets error:', error.response?.data || error.message);
      throw error;
    }
  },

  deleteOrder: async (orderId) => {
    const response = await api.delete(`/EventRegistration/API/User/order/delete/${orderId}`);
    return response.data;
  },
};