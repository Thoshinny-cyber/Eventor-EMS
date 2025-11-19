import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { eventService } from '../services/eventService';
import { wishlistService } from '../services/wishlistService';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';

const EventDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, user, isUser } = useAuth();
  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [ticketCount, setTicketCount] = useState(1);
  const [isInWishlist, setIsInWishlist] = useState(false);

  useEffect(() => {
    loadEvent();
    if (isAuthenticated && isUser && id) {
      setIsInWishlist(wishlistService.isInWishlist(parseInt(id)));
    }
  }, [id, isAuthenticated, isUser]);

  const loadEvent = async () => {
    try {
      const data = await eventService.getEventById(id);
      setEvent(data);
    } catch (error) {
      toast.error('Failed to load event details');
      navigate('/events');
    } finally {
      setLoading(false);
    }
  };

  const handleSeatSelection = (seat) => {
    if (selectedSeats.includes(seat)) {
      setSelectedSeats(selectedSeats.filter((s) => s !== seat));
    } else {
      if (selectedSeats.length < ticketCount) {
        setSelectedSeats([...selectedSeats, seat]);
      } else {
        toast.warning(`You can only select ${ticketCount} seat(s)`);
      }
    }
  };

  const handleBookTickets = () => {
    if (!isAuthenticated) {
      toast.error('Please login to book tickets');
      navigate('/login');
      return;
    }

    if (selectedSeats.length === 0) {
      toast.error('Please select at least one seat');
      return;
    }

    if (selectedSeats.length !== ticketCount) {
      toast.error(`Please select exactly ${ticketCount} seat(s)`);
      return;
    }
    // DEBUG: Check what's being passed
  console.log('Navigating to /booking with state:', {
    event,
    selectedSeats,
    ticketCount,
  });

    navigate('/booking', {
      state: {
        event,
        selectedSeats,
        ticketCount,
      },
    });
  };

  if (loading) {
    return <div className="loading">Loading event details...</div>;
  }

  if (!event) {
    return <div className="error">Event not found</div>;
  }

  // Note: event.seats contains booked seats, not available seats
  // For a real implementation, you'd need an endpoint to get available seats
  // For now, we'll generate seat options based on available tickets
  const generateSeatOptions = () => {
    const seats = [];
    const rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
    const bookedSeats = new Set(event.seats || []);
    
    for (let row of rows) {
      for (let num = 1; num <= 20; num++) {
        const seat = `${row}${num}`;
        if (!bookedSeats.has(seat) && seats.length < event.availableTickets) {
          seats.push(seat);
        }
      }
    }
    return seats;
  };

  const availableSeats = generateSeatOptions();
  const totalPrice = event.price * ticketCount;

  return (
    <div className="event-detail-container">
      <Link to="/events" className="back-link">
        ← Back to Events
      </Link>

      <div className="event-detail-header">
        <div className="event-detail-info">
          <div className="event-title-row">
            <h1>{event.name}</h1>
            {isAuthenticated && isUser && (
              <button
                className={`wishlist-btn-large ${isInWishlist ? 'active' : ''}`}
                onClick={() => {
                  if (isInWishlist) {
                    wishlistService.removeFromWishlist(event.id);
                    toast.success('Removed from wishlist');
                  } else {
                    wishlistService.addToWishlist(event.id);
                    toast.success('Added to wishlist');
                  }
                  setIsInWishlist(!isInWishlist);
                }}
                title={isInWishlist ? 'Remove from wishlist' : 'Add to wishlist'}
              >
                {isInWishlist ? '❤️' : '🤍'}
              </button>
            )}
          </div>
          <p className="event-host">Host: {event.host}</p>
          <p className="event-venue">Venue: {event.venue}</p>
          <p className="event-date">
            Date: {new Date(event.date).toLocaleDateString()}
          </p>
          <p className="event-price">Price: {event.price}SGD</p>
          <p className="event-tickets">
            Available Tickets: {event.availableTickets}
          </p>
        </div>
      </div>

      <div className="event-description">
        <h2>Description</h2>
        <p>{event.description}</p>
      </div>

      {isAuthenticated && (
        <div className="booking-section">
          <div className="ticket-selection">
            <label>
              Number of Tickets:
              <input
                type="number"
                min="1"
                max={event.availableTickets}
                value={ticketCount}
                onChange={(e) => {
                  const count = parseInt(e.target.value);
                  setTicketCount(count);
                  if (selectedSeats.length > count) {
                    setSelectedSeats(selectedSeats.slice(0, count));
                  }
                }}
              />
            </label>
            <p className="total-price">Total: ${totalPrice}</p>
          </div>

          {availableSeats.length > 0 && (
            <div className="seating-section">
              <h3>Select Seats</h3>
              <div className="seats-grid">
                {availableSeats.map((seat, index) => {
                  const isSelected = selectedSeats.includes(seat);
                  return (
                    <button
                      key={index}
                      className={`seat-button ${isSelected ? 'selected' : ''}`}
                      onClick={() => handleSeatSelection(seat)}
                    >
                      {seat}
                    </button>
                  );
                })}
              </div>
              <p className="selected-seats-info">
                Selected: {selectedSeats.join(', ') || 'None'}
              </p>
            </div>
          )}

          <button
            className="btn-primary btn-book"
            onClick={handleBookTickets}
            disabled={selectedSeats.length === 0 || selectedSeats.length !== ticketCount}
          >
            Book Tickets
          </button>
        </div>
      )}

      {!isAuthenticated && (
        <div className="login-prompt">
          <p>Please <Link to="/login">login</Link> to book tickets</p>
        </div>
      )}
    </div>
  );
};

export default EventDetail;

