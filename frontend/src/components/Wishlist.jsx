import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { eventService } from '../services/eventService';
import { wishlistService } from '../services/wishlistService';
import { toast } from 'react-toastify';

const Wishlist = () => {
  const [wishlistIds, setWishlistIds] = useState([]);
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadWishlist();
  }, []);

  const loadWishlist = async () => {
    try {
      const ids = wishlistService.getWishlist();
      setWishlistIds(ids);

      if (ids.length > 0) {
        const allEvents = await eventService.getAllEvents();
        const wishlistEvents = allEvents.filter((event) =>
          ids.includes(event.id)
        );
        setEvents(wishlistEvents);
      }
    } catch (error) {
      toast.error('Failed to load wishlist');
    } finally {
      setLoading(false);
    }
  };

  const removeFromWishlist = (eventId) => {
    wishlistService.removeFromWishlist(eventId);
    setWishlistIds(wishlistService.getWishlist());
    setEvents(events.filter((event) => event.id !== eventId));
    toast.success('Removed from wishlist');
  };

  if (loading) {
    return <div className="loading">Loading wishlist...</div>;
  }

  return (
    <div className="wishlist-container">
      <h1>My Wishlist</h1>

      {events.length === 0 ? (
        <div className="no-wishlist">
          <p>Your wishlist is empty.</p>
          <Link to="/events" className="btn-primary">
            Browse Events
          </Link>
        </div>
      ) : (
        <div className="events-grid">
          {events.map((event) => (
            <div key={event.id} className="event-card">
              <div className="event-card-content">
                <div className="event-card-header">
                  <h3>{event.name}</h3>
                  <button
                    className="wishlist-btn active"
                    onClick={() => removeFromWishlist(event.id)}
                    title="Remove from wishlist"
                  >
                    ❤️
                  </button>
                </div>
                <p className="event-venue">{event.venue}</p>
                <p className="event-date">
                  {new Date(event.date).toLocaleDateString()}
                </p>
                <p className="event-price">{event.price}SGD</p>
                <p className="event-tickets">
                  {event.availableTickets} tickets available
                </p>
                <Link to={`/events/${event.id}`} className="btn-primary">
                  View Details
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Wishlist;

