import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { adminService } from '../services/adminService';
import { toast } from 'react-toastify';

const AdminDashboard = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalEvents: 0,
    totalTickets: 0,
    soldTickets: 0,
    revenue: 0,
  });

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = async () => {
    try {
      const data = await adminService.getAllEvents();
      setEvents(data);
      calculateStats(data);
    } catch (error) {
      toast.error('Failed to load events');
    } finally {
      setLoading(false);
    }
  };

  const calculateStats = (eventsList) => {
    const totalEvents = eventsList.length;
    let totalTickets = 0;
    let soldTickets = 0;
    let revenue = 0;

    eventsList.forEach((event) => {
      totalTickets += event.availableTickets || 0;
      soldTickets += event.soldTickets || 0;
      revenue += (event.price || 0) * (event.soldTickets || 0);
    });

    setStats({
      totalEvents,
      totalTickets,
      soldTickets,
      revenue,
    });
  };

  const handleDelete = async (id, eventName) => {
    if (!window.confirm(`Are you sure you want to delete "${eventName}"?`)) {
      return;
    }

    try {
      await adminService.deleteEvent(id);
      toast.success('Event deleted successfully');
      loadEvents();
    } catch (error) {
      toast.error('Failed to delete event');
    }
  };

  if (loading) {
    return <div className="loading">Loading dashboard...</div>;
  }

  return (
    <div className="admin-dashboard">
      <div className="admin-header">
        <h1>Admin Dashboard</h1>
        <Link to="/admin/events/create" className="btn-primary">
          + Create New Event
        </Link>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <h3>Total Events</h3>
          <p className="stat-value">{stats.totalEvents}</p>
        </div>
        <div className="stat-card">
          <h3>Total Tickets</h3>
          <p className="stat-value">{stats.totalTickets}</p>
        </div>
        <div className="stat-card">
          <h3>Sold Tickets</h3>
          <p className="stat-value">{stats.soldTickets}</p>
        </div>
        <div className="stat-card">
          <h3>Revenue</h3>
          <p className="stat-value">S${stats.revenue.toLocaleString()}</p>
        </div>
      </div>

      <div className="events-section">
        <h2>All Events</h2>
        <div className="events-table-container">
          <table className="events-table">
            <thead>
              <tr>
                <th>Event Name</th>
                <th>Venue</th>
                <th>Date</th>
                <th>Price</th>
                <th>Available</th>
                <th>Sold</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {events.length === 0 ? (
                <tr>
                  <td colSpan="7" className="no-data">
                    No events found. Create your first event!
                  </td>
                </tr>
              ) : (
                events.map((event) => (
                  <tr key={event.id}>
                    <td>{event.name}</td>
                    <td>{event.venue}</td>
                    <td>{new Date(event.date).toLocaleDateString()}</td>
                    <td>S${event.price}</td>
                    <td>{event.availableTickets}</td>
                    <td>{event.soldTickets || 0}</td>
                    <td>
                      <div className="action-buttons">
                        <Link
                          to={`/admin/events/edit/${event.id}`}
                          className="btn-secondary btn-sm"
                        >
                          Edit
                        </Link>
                        <button
                          className="btn-danger btn-sm"
                          onClick={() => handleDelete(event.id, event.name)}
                        >
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;

