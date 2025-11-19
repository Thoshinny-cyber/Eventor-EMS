import api from '../utils/api';

export const discountService = {
  getAllDiscounts: async () => {
    const response = await api.get('/discounts');
    return response.data || [];
  },
};

