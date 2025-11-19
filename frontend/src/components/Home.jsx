import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { eventService } from '../services/eventService';


const Home = () => {
  const [topEvents, setTopEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadTopEvents = async () => {
      try {
        const data = await eventService.getTopEvents();
        setTopEvents(data);
      } catch (error) {
        console.error('Failed to load top events');
      } finally {
        setLoading(false);
      }
    };

    loadTopEvents();
  }, []);

  if (loading) {
    return <div className="loading">Loading amazing events...</div>;
  }

  return (
    <div className="home-container">
      {/* Hero Section */}
      <section className="hero-section">
        <h1>Welcome to Eventor</h1>
        <p>Discover and book tickets for the best events in town</p>
        <Link to="/events" className="btn-primary btn-large">
          Browse All Events
        </Link>
      </section>

      {/* Top Events Section */}
      {topEvents.length > 0 && (
        <section className="top-events-section">
          <h2>Top Events Right Now</h2>
          <div className="events-grid">
            {topEvents.map((event) => (
              <div key={event.id} className="event-card">
                <div className="event-card-content">
                  <h3>{event.name}</h3>
                  <p className="event-venue">
                    <strong>Venue:</strong> {event.venue}
                  </p>
                  <p className="event-date">
                    {new Date(event.date).toLocaleDateString('en-US', {
                      weekday: 'long',
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                    })}
                  </p>
                  <p className="event-price">From S${event.price}</p>
                  <Link to={`/events/${event.id}`} className="btn-primary">
                    View Details
                  </Link>
                </div>
              </div>
            ))}
          </div>

          <div className="text-center" style={{ marginTop: '40px' }}>
            <Link to="/events" className="btn-secondary btn-large">
              See All Events
            </Link>
          </div>
        </section>
      )}

      {/* Optional: Empty state if no top events */}
      {topEvents.length === 0 && !loading && (
        <section className="empty-section">
          <p>No events available at the moment. Check back soon!</p>
          <Link to="/events" className="btn-primary">
            Explore Events
          </Link>
        </section>
      )}
    </div>
  );
};

export default Home;
