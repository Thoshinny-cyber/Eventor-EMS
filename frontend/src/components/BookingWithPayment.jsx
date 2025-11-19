// src/components/BookingWithPayment.jsx
import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { paymentServiceBackend } from '../services/paymentServiceBackend';
import { orderService } from '../services/orderService';
import { toast } from 'react-toastify';
import { loadStripe } from '@stripe/stripe-js';
import {
  Elements,
  CardElement,
  useStripe,
  useElements,
} from '@stripe/react-stripe-js';

const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY);

/* --------------------------------------------------------------- */
/*  Inner component – payment + order creation                     */
/* --------------------------------------------------------------- */
const BookingWithPaymentInner = ({
  event,
  selectedSeats,
  ticketCount,
  totalPrice,
}) => {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);

  const handlePayment = async () => {
    if (!stripe || !elements) return;

setLoading(true);
  try {
    // 1. Create PaymentIntent
    const result = await paymentServiceBackend.createIntent(null, totalPrice);
    
    console.log('createIntent result:', result); // ← Should show { client_secret: "pi_..." }

    const client_secret = result?.client_secret;
    if (!client_secret) {
      toast.error('Payment initialization failed – no client secret');
      setLoading(false);
      return;
    }

    // 2. Confirm payment
    const { error, paymentIntent } = await stripe.confirmCardPayment(client_secret, {
      payment_method: { card: elements.getElement(CardElement) },
    });

    if (error) {
      toast.error(error.message ?? 'Payment failed');
      setLoading(false);
      return;
    }

    if (paymentIntent.status === 'succeeded') {
      // 3. Create order
      // const orderData = {
      //   userId: user.id,
      //   eventId: event.id,
      //   count: ticketCount,
      //   totalPrice,
      //   paymentStatus: 'PAID',
      //   bookedSeats: selectedSeats.map(seat => ({
      //     seatNumber: seat,
      //     isSeatBooked: true,
      //   })),
      // };
const orderData = {
  userId: user.id,
  eventId: event.id,
  count: ticketCount,
  totalPrice: totalPrice,
  paymentStatus: 'PAID',
  bookedSeats: selectedSeats, // ← just array of strings: ["A1", "A2"]
};
      const orderResponse = await orderService.bookTickets(orderData);
      console.log('Order created:', orderResponse);
      await paymentServiceBackend.finalizeOrder(orderResponse.id, paymentIntent.id);
      toast.success('Booking confirmed! Tickets booked.');
      navigate('/my-events', { state: { refresh: true } });
    }
  } catch (err) {
      console.error('Payment error:', err);
      toast.error('Payment failed');
  } finally {
      setLoading(false);
  }
};

  return (
    <div style={{ maxWidth: '420px', margin: '2rem auto', fontFamily: 'Arial, sans-serif' }}>
      <h2>Complete Payment</h2>
      <p>
        Total: <strong>{totalPrice} SGD</strong>
      </p>

      <div
        style={{
          padding: '12px',
          border: '1px solid #ccc',
          borderRadius: '6px',
          marginBottom: '16px',
        }}
      >
        <CardElement />
      </div>

      <button
        onClick={handlePayment}
        disabled={loading || !stripe}
        style={{
          width: '100%',
          padding: '12px',
          background: loading ? '#999' : '#28a745',
          color: '#fff',
          border: 'none',
          borderRadius: '4px',
          fontSize: '1rem',
          cursor: loading ? 'not-allowed' : 'pointer',
        }}
      >
        {loading ? 'Processing…' : `Pay ${totalPrice} SGD`}
      </button>
    </div>
  );
};

/* --------------------------------------------------------------- */
/*  Outer component – read state & guard                           */
/* --------------------------------------------------------------- */
const BookingWithPayment = () => {
  const { state } = useLocation();
  const { user } = useAuth();
  const navigate = useNavigate();

  const { event, selectedSeats, ticketCount } = state || {};

  /* ---- DEBUG LOG (remove later if you want) ---- */
  useEffect(() => {
    console.log('BookingWithPayment state:', { event, selectedSeats, ticketCount, user });
  }, [event, selectedSeats, ticketCount, user]);

  /* ---- Guard: missing data → back to events ---- */
  useEffect(() => {
    if (!event || !selectedSeats || selectedSeats.length === 0 || !ticketCount || !user) {
      toast.error('Invalid booking data – redirecting to events');
      navigate('/events');
    }
  }, [event, selectedSeats, ticketCount, user, navigate]);

  if (!event || !selectedSeats || !ticketCount) return null;

  const totalPrice = event.price * ticketCount;

  return (
    <Elements stripe={stripePromise}>
      <BookingWithPaymentInner
        event={event}
        selectedSeats={selectedSeats}
        ticketCount={ticketCount}
        totalPrice={totalPrice}
      />
    </Elements>
  );
};

export default BookingWithPayment;