import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { orderService } from '../services/orderService';
import { toast } from 'react-toastify';

const Booking = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);

  const { event, selectedSeats, ticketCount } = location.state || {};

  useEffect(() => {
    if (!event || !user) {
      navigate('/events');
    }
  }, [event, user, navigate]);

  const handleConfirmBooking = async () => {
    if (!event || !user || !selectedSeats || selectedSeats.length === 0) {
      toast.error('Invalid booking data');
      return;
    }

    setLoading(true);

    try {
      const orderData = {
        userId: user.id,
        eventId: event.id,
        count: ticketCount,
        bookedSeats: selectedSeats.map((seat) => ({ 
          seatNumber: seat,
          isSeatBooked: true,
          eventId: event.id,
          userId: user.id
        })),
      };

      await orderService.bookTickets(orderData);
      toast.success('Tickets booked successfully!');
      navigate('/orders');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to book tickets');
    } finally {
      setLoading(false);
    }
  };

  if (!event || !user) {
    return null;
  }

  const totalPrice = event.price * ticketCount;

  return (
    <div className="booking-container">
      <h1>Confirm Your Booking</h1>

      <div className="booking-summary">
        <div className="booking-event-info">
          <h2>{event.name}</h2>
          <p><strong>Venue:</strong> {event.venue}</p>
          <p><strong>Date:</strong> {new Date(event.date).toLocaleDateString()}</p>
          <p><strong>Host:</strong> {event.host}</p>
        </div>

        <div className="booking-details">
          <h3>Booking Details</h3>
          <p><strong>Number of Tickets:</strong> {ticketCount}</p>
          <p><strong>Selected Seats:</strong> {selectedSeats.join(', ')}</p>
          <p><strong>Price per Ticket:</strong> S${event.price}</p>
          <p className="total-price"><strong>Total Price:</strong> S${totalPrice}</p>
        </div>

        <div className="user-info">
          <h3>User Information</h3>
          <p><strong>Name:</strong> {user.name}</p>
          <p><strong>Email:</strong> {user.email || 'N/A'}</p>
          <p><strong>Username:</strong> {user.username}</p>
        </div>
      </div>

      <div className="booking-actions">
        <button
          className="btn-secondary"
          onClick={() => navigate(-1)}
          disabled={loading}
        >
          Go Back
        </button>
        <button
          className="btn-primary"
          onClick={handleConfirmBooking}
          disabled={loading}
        >
          {loading ? 'Processing...' : 'Confirm Booking'}
        </button>
      </div>
    </div>
  );
};

export default Booking;

