import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { eventService } from '../services/eventService';
import { categoryService } from '../services/categoryService';
import { wishlistService } from '../services/wishlistService';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';

const EventList = () => {
  const { isAuthenticated, isUser } = useAuth();
  const [events, setEvents] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [wishlist, setWishlist] = useState([]);
  const [filters, setFilters] = useState({
    fromDate: '',
    toDate: '',
    minPrice: '',
    maxPrice: '',
    checkedCategory: [],
    checkedVenue: [],
  });
  const [showFilters, setShowFilters] = useState(false);

  useEffect(() => {
    loadEvents();
    loadCategories();
    if (isAuthenticated && isUser) {
      setWishlist(wishlistService.getWishlist());
    }
  }, [isAuthenticated, isUser]);

  const loadEvents = async () => {
    try {
      const data = await eventService.getAllEvents();
      setEvents(data);
    } catch (error) {
      toast.error('Failed to load events');
    } finally {
      setLoading(false);
    }
  };

  const loadCategories = async () => {
    try {
      const data = await categoryService.getAllCategories();
      setCategories(data);
    } catch (error) {
      console.error('Failed to load categories');
    }
  };

  const handleFilterChange = (e) => {
    const { name, value, type, checked } = e.target;
    if (type === 'checkbox') {
      const categoryId = parseInt(value);
      setFilters((prev) => ({
        ...prev,
        checkedCategory: checked
          ? [...prev.checkedCategory, categoryId]
          : prev.checkedCategory.filter((id) => id !== categoryId),
      }));
    } else {
      setFilters((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  const applyFilters = async () => {
    setLoading(true);
    try {
      const filterData = {
        fromDate: filters.fromDate || null,
        toDate: filters.toDate || null,
        minPrice: filters.minPrice ? parseInt(filters.minPrice) : 0,
        maxPrice: filters.maxPrice ? parseInt(filters.maxPrice) : 999999,
        checkedCategory: filters.checkedCategory,
        checkedVenue: filters.checkedVenue,
      };
      const data = await eventService.getFilteredEvents(filterData);
      setEvents(data);
      setShowFilters(false);
    } catch (error) {
      toast.error('Failed to filter events');
    } finally {
      setLoading(false);
    }
  };

  const clearFilters = () => {
    setFilters({
      fromDate: '',
      toDate: '',
      minPrice: '',
      maxPrice: '',
      checkedCategory: [],
      checkedVenue: [],
    });
    loadEvents();
  };

  const toggleWishlist = (eventId) => {
    if (!isAuthenticated || !isUser) {
      toast.error('Please login to add events to wishlist');
      return;
    }

    if (wishlistService.isInWishlist(eventId)) {
      wishlistService.removeFromWishlist(eventId);
      toast.success('Removed from wishlist');
    } else {
      wishlistService.addToWishlist(eventId);
      toast.success('Added to wishlist');
    }
    setWishlist(wishlistService.getWishlist());
  };

  if (loading && events.length === 0) {
    return <div className="loading">Loading events...</div>;
  }

  return (
    <div className="event-list-container">
      <div className="event-list-header">
        <h1>Events</h1>
        <button
          className="btn-secondary"
          onClick={() => setShowFilters(!showFilters)}
        >
          {showFilters ? 'Hide Filters' : 'Show Filters'}
        </button>
      </div>

      {showFilters && (
        <div className="filters-panel">
          <div className="filter-group">
            <label>From Date</label>
            <input
              type="date"
              name="fromDate"
              value={filters.fromDate}
              onChange={handleFilterChange}
            />
          </div>
          <div className="filter-group">
            <label>To Date</label>
            <input
              type="date"
              name="toDate"
              value={filters.toDate}
              onChange={handleFilterChange}
            />
          </div>
          <div className="filter-group">
            <label>Min Price</label>
            <input
              type="number"
              name="minPrice"
              value={filters.minPrice}
              onChange={handleFilterChange}
              min="0"
            />
          </div>
          <div className="filter-group">
            <label>Max Price</label>
            <input
              type="number"
              name="maxPrice"
              value={filters.maxPrice}
              onChange={handleFilterChange}
              min="0"
            />
          </div>
          <div className="filter-group">
            <label>Categories</label>
            <div className="checkbox-group">
              {categories.map((category) => (
                <label key={category.id} className="checkbox-label">
                  <input
                    type="checkbox"
                    value={category.id}
                    checked={filters.checkedCategory.includes(category.id)}
                    onChange={handleFilterChange}
                  />
                  {category.name}
                </label>
              ))}
            </div>
          </div>
          <div className="filter-actions">
            <button className="btn-primary" onClick={applyFilters}>
              Apply Filters
            </button>
            <button className="btn-secondary" onClick={clearFilters}>
              Clear
            </button>
          </div>
        </div>
      )}

      <div className="events-grid">
        {events.length === 0 ? (
          <p className="no-events">No events found</p>
        ) : (
          events.map((event) => (
            <div key={event.id} className="event-card">
              <div className="event-card-content">
                <div className="event-card-header">
                  <h3>{event.name}</h3>
                  {isAuthenticated && isUser && (
                    <button
                      className={`wishlist-btn ${wishlistService.isInWishlist(event.id) ? 'active' : ''}`}
                      onClick={() => toggleWishlist(event.id)}
                      title={wishlistService.isInWishlist(event.id) ? 'Remove from wishlist' : 'Add to wishlist'}
                    >
                      {wishlistService.isInWishlist(event.id) ? '❤️' : '🤍'}
                    </button>
                  )}
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
          ))
        )}
      </div>
    </div>
  );
};

export default EventList;

