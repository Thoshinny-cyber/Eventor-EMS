import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { adminService } from '../services/adminService';
import { categoryService } from '../services/categoryService';
import { toast } from 'react-toastify';

const AdminEventForm = () => {
  const { id } = useParams();
  const isEdit = !!id;
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    venue: '',
    date: '',
    host: '',
    price: '',
    availableTickets: '',
    categoryId: '',
    image: null,
  });

  useEffect(() => {
    loadCategories();
    if (isEdit) {
      loadEvent();
    }
  }, [id]);

  const loadCategories = async () => {
    try {
      const data = await categoryService.getAllCategories();
      console.log('Categories set in AdminEventForm:', data);
      setCategories(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Error in loadCategories:', error);
      toast.error('Failed to load categories');
      setCategories([]);
    }
  };

  const loadEvent = async () => {
    try {
      const event = await adminService.getEventById(id);
      console.log('Loaded event:', event);
      setFormData({
        name: event.name || '',
        description: event.description || '',
        venue: event.venue || '',
        date: event.date ? event.date.split('T')[0] : '',
        host: event.host || '',
        price: event.price || '',
        availableTickets: event.availableTickets || '',
        categoryId: event.category?.id || '',
        image: null,
      });
    } catch (error) {
      console.error('Error loading event:', error);
      toast.error('Failed to load event');
      navigate('/admin');
    }
  };

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === 'image') {
      setFormData({ ...formData, image: files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
if (isEdit) {
  const updateData = {
    id: parseInt(id),
    name: formData.name,
    description: formData.description,
    venue: formData.venue,
    date: formData.date,
    host: formData.host,
    price: parseInt(formData.price),
    availableTickets: parseInt(formData.availableTickets),
    categoryId: formData.categoryId ? parseInt(formData.categoryId) : null,
    image: formData.image ? null : (event?.image || null) /// keep old filename
  };

  console.log('SENDING UPDATE:', updateData);
  await adminService.updateEvent(updateData);

        toast.success('Event updated successfully');
      } else {
        const formDataToSend = new FormData();
        if (formData.image) {
          formDataToSend.append('image', formData.image);
        }
        formDataToSend.append('id', '0');
        formDataToSend.append('name', formData.name);
        formDataToSend.append('description', formData.description);
        formDataToSend.append('venue', formData.venue);
        formDataToSend.append('date', formData.date);
        formDataToSend.append('host', formData.host);
        formDataToSend.append('price', formData.price);
        formDataToSend.append('availableTickets', formData.availableTickets);
        formDataToSend.append('categoryId', formData.categoryId);

        await adminService.createEvent(formDataToSend);
        toast.success('Event created successfully');
      }
      navigate('/admin');
    } catch (error) {
      toast.error(isEdit ? 'Failed to update event' : 'Failed to create event');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="admin-event-form">
      <div className="form-header">
        <h1>{isEdit ? 'Edit Event' : 'Create New Event'}</h1>
        <button
          className="btn-secondary"
          onClick={() => navigate('/admin')}
        >
          ← Back to Dashboard
        </button>
      </div>

      <form onSubmit={handleSubmit} className="event-form">
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="name">Event Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="host">Host *</label>
            <input
              type="text"
              id="host"
              name="host"
              value={formData.host}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="description">Description *</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="4"
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="venue">Venue *</label>
            <input
              type="text"
              id="venue"
              name="venue"
              value={formData.venue}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="date">Event Date *</label>
            <input
              type="date"
              id="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              required
              min={new Date().toISOString().split('T')[0]}
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="price">Price ($S) *</label>
            <input
              type="number"
              id="price"
              name="price"
              value={formData.price}
              onChange={handleChange}
              required
              min="0"
            />
          </div>
          <div className="form-group">
            <label htmlFor="availableTickets">Available Tickets (Capacity) *</label>
            <input
              type="number"
              id="availableTickets"
              name="availableTickets"
              value={formData.availableTickets}
              onChange={handleChange}
              required
              min="1"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="categoryId">Category *</label>
            <select
              id="categoryId"
              name="categoryId"
              value={formData.categoryId}
              onChange={handleChange}
              required
            >
              <option value="">Select Category</option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="image">Event Image {!isEdit && '*'}</label>
            <input
              type="file"
              id="image"
              name="image"
              accept="image/*"
              onChange={handleChange}
              required={!isEdit}
            />
            {isEdit && (
              <small>Leave empty to keep current image</small>
            )}
          </div>
        </div>

        <div className="form-actions">
          <button
            type="button"
            className="btn-secondary"
            onClick={() => navigate('/admin')}
            disabled={loading}
          >
            Cancel
          </button>
          <button
            type="submit"
            className="btn-primary"
            disabled={loading}
          >
            {loading
              ? isEdit
                ? 'Updating...'
                : 'Creating...'
              : isEdit
              ? 'Update Event'
              : 'Create Event'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default AdminEventForm;