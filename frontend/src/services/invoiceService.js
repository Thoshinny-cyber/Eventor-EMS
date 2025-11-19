import api from '../utils/api';

export const invoiceService = {
  getInvoiceByOrderId: async (orderId) => {
    const response = await api.get(`/EventRegistration/API/User/Invoice/order/${orderId}`);
    return response || null; // Interceptor handles data.data
  },

  getInvoicesByUserId: async (userId) => {
    const response = await api.get(`/EventRegistration/API/User/Invoice/user/${userId}`);
    return response || []; // Interceptor handles data.data
  },

  getInvoiceByNumber: async (invoiceNumber) => {
    const response = await api.get(`/EventRegistration/API/User/Invoice/number/${invoiceNumber}`);
    return response || null; // Interceptor handles data.data
  },

  // ADD: PDF Download (uses blob for binary)
  downloadTicketPdf: async (orderId) => {
    const response = await api.get(
      `/EventRegistration/API/User/Invoice/download/ticket/${orderId}`,
      { responseType: 'blob' } // Binary for PDF
    );
    return response; // Raw blob
  },
};