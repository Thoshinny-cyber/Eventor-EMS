// Razorpay Payment Service
// Make sure to add Razorpay script in index.html

export const paymentService = {
  loadRazorpayScript: () => {
    return new Promise((resolve) => {
      if (window.Razorpay) {
        resolve(true);
        return;
      }

      const script = document.createElement('script');
      script.src = 'https://checkout.razorpay.com/v1/checkout.js';
      script.onload = () => resolve(true);
      script.onerror = () => resolve(false);
      document.body.appendChild(script);
    });
  },

  createOrder: async (amount, currency = 'SGD', orderId, description) => {
    // In a real app, you'd call your backend to create a Razorpay order
    // For now, returning a mock order
    return {
      id: orderId || `order_${Date.now()}`,
      amount: amount, // Razorpay expects amount in cents (SGD)
      currency,
      description,
    };
  },

  initiatePayment: async (orderData, options = {}) => {
    await paymentService.loadRazorpayScript();

    if (!window.Razorpay) {
      throw new Error('Razorpay SDK failed to load');
    }

    const razorpayOptions = {
      key: import.meta.env.VITE_RAZORPAY_KEY_ID|| 'YOUR_RAZORPAY_KEY_ID', // Add your Razorpay key
      amount: orderData.amount,
      currency: orderData.currency,
      name: 'Event Registration System',
      description: orderData.description || 'Event Ticket Booking',
      order_id: orderData.id,
      handler: options.onSuccess || (() => {}),
      prefill: {
        name: options.customerName || '',
        email: options.customerEmail || '',
        contact: options.customerPhone || '',
      },
      theme: {
        color: '#4a90e2',
      },
      modal: {
        ondismiss: options.onDismiss || (() => {}),
      },
    };

    const razorpay = new window.Razorpay(razorpayOptions);
    razorpay.open();

    return razorpay;
  },
};

