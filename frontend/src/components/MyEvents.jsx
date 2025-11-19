import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { orderService } from '../services/orderService';
import { adminService } from '../services/adminService';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';
import api from '../utils/api';

const MyEvents = () => {
  const { user, isAdmin, isUser } = useAuth();
  const location = useLocation();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  // Load events on mount or user change
  useEffect(() => {
    if (user?.id) {
      loadEvents();
    }
  }, [user, isAdmin, isUser]);

  // Auto-refresh after successful booking
  useEffect(() => {
    if (location.state?.refresh) {
      loadEvents();
      toast.success('Booking confirmed! Welcome to your events.');
      window.history.replaceState({}, document.title);
    }
  }, [location.state]);

  const loadEvents = async () => {
    try {
      if (isAdmin) {
        const data = await adminService.getAllEvents();
        setEvents(data);
      } else if (isUser) {
        const data = await orderService.getUserOrders(user.id);
        // Sort by latest booking first
        const sorted = [...data].sort((a, b) => new Date(b.date) - new Date(a.date));
        setEvents(sorted);
      }
    } catch (error) {
      toast.error('Failed to load your events');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteOrder = async (orderId) => {
    if (!window.confirm('Cancel this booking? This cannot be undone.')) return;

    try {
      await orderService.deleteOrder(orderId);
      toast.success('Booking cancelled successfully');
      loadEvents();
    } catch (error) {
      toast.error('Failed to cancel booking');
    }
  };

  const handleDownloadTicket = async (orderId) => {
    try {
      const response = await api.get(
        `/EventRegistration/API/User/Invoice/download/ticket/${orderId}`,
        { responseType: 'blob' }
      );
      const url = window.URL.createObjectURL(new Blob([response]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `ticket-${orderId}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      toast.success('Ticket downloaded!');
    } catch (error) {
      toast.error('Failed to download ticket');
    }
  };

  if (loading) {
    return <div className="loading">Loading your events...</div>;
  }

  return (
    <div className="my-events-container">
      <h1>{isAdmin ? 'My Events (Vendor Dashboard)' : 'My Bookings'}</h1>

      {events.length === 0 ? (
        <div className="no-events">
          <p>
            {isAdmin
              ? 'You haven\'t created any events yet.'
              : 'You have no bookings yet.'}
          </p>
          {isAdmin ? (
            <Link to="/admin/events/create" className="btn-primary">
              Create Your First Event
            </Link>
          ) : (
            <Link to="/events" className="btn-primary">
              Explore Events
            </Link>
          )}
        </div>
      ) : (
        <div className="events-list">
          {events.map((item) => {
            const isOrder = !!item.eventName || !!item.count;

            return (
              <div key={item.id} className="event-card">
                <div className="event-card-content">
                  <div className="event-header">
                    <h3>{isOrder ? item.eventName : item.name}</h3>
                    {isOrder && item.paymentStatus && (
                      <span className={`payment-status ${item.paymentStatus.toLowerCase()}`}>
                        {item.paymentStatus}
                      </span>
                    )}
                  </div>

                  {isOrder ? (
                    /* User Booking View */
                    <>
                      <p className="event-venue">
                        <strong>Venue:</strong> {item.venue || 'To be announced'}
                      </p>
                      <p className="event-date">
                        <strong>Date:</strong>{' '}
                        {new Date(item.date).toLocaleDateString('en-US', {
                          weekday: 'long',
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric'
                        })}
                      </p>
                      <p><strong>Tickets:</strong> {item.count}</p>
                      {item.bookedSeatsString && item.bookedSeatsString.length > 0 && (
                        <p><strong>Seats:</strong> {item.bookedSeatsString.join(', ')}</p>
                      )}
                      <p><strong>Total Paid:</strong> S${item.totalPrice}</p>

                      <div className="event-actions">
                        {item.paymentStatus === 'PAID' ? (
                          <button
                            className="btn-success"
                            onClick={() => handleDownloadTicket(item.id)}
                          >
                            Download Ticket (PDF)
                          </button>
                        ) : (
                          <button
                            className="btn-danger"
                            onClick={() => handleDeleteOrder(item.id)}
                          >
                            Cancel Booking
                          </button>
                        )}
                      </div>
                    </>
                  ) : (
                    /* Admin Event View */
                    <>
                      <p className="event-venue"><strong>Venue:</strong> {item.venue}</p>
                      <p className="event-date">
                        {new Date(item.date).toLocaleDateString()}
                      </p>
                      <p className="event-price">Price: S${item.price}</p>
                      <p className="event-tickets">
                        {item.availableTickets} tickets available
                      </p>
                      <div className="event-actions">
                        <Link
                          to={`/admin/events/edit/${item.id}`}
                          className="btn-primary"
                        >
                          Edit Event
                        </Link>
                      </div>
                    </>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default MyEvents;
