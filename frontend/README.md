# Event Registration Frontend

React frontend application for the Event Registration System.

## Features

- User Authentication (Login/Register)
- Browse Events with filtering
- View Event Details
- Book Tickets with Seat Selection
- View Order History
- User Profile Management
- Responsive Design

## Tech Stack

- React 18
- React Router DOM
- Axios for API calls
- React Toastify for notifications
- Vite as build tool

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Backend Spring Boot application running on `http://localhost:8080`

### Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

### Build for Production

```bash
npm run build
```

The built files will be in the `dist` directory.


## API Configuration

The frontend is configured to communicate with the backend at `http://localhost:8080`. 

The API base URL can be changed in `src/utils/api.js`:

```javascript
const API_BASE_URL = 'http://localhost:8080';
```

## Authentication

The application uses HTTP Basic Authentication. After login, credentials are stored in localStorage and automatically included in API requests.

## Routes

- `/` - Home page with top events and categories
- `/login` - User login
- `/register` - User registration
- `/events` - List all events with filtering
- `/events/:id` - Event details page
- `/booking` - Ticket booking confirmation (protected)
- `/orders` - User's order history (protected)
- `/profile` - User profile management (protected)

## Features in Detail

### Event Browsing
- View all events in a grid layout
- Filter events by date range, price range, and categories
- View top events on the home page
- Browse events by category

### Event Details
- View comprehensive event information
- Select number of tickets
- Select specific seats (if available)
- Book tickets (requires authentication)

### Order Management
- View all past orders
- See order details including seats and total price
- Cancel orders

### Profile Management
- View and edit user profile
- Update profile picture
- Change password

## Development

### Adding New Features

1. Create new components in `src/components/`
2. Add API services in `src/services/` if needed
3. Update routes in `src/App.jsx`
4. Add styling in `src/App.css`

### Environment Variables

You can create a `.env` file for environment-specific configuration:

```
VITE_API_BASE_URL=http://localhost:8080
```

Then use it in your code:
```javascript
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
```

## Troubleshooting

### CORS Issues
Make sure the backend CORS configuration allows requests from `http://localhost:3000`

### Authentication Issues
- Check that credentials are being stored correctly in localStorage
- Verify the backend authentication endpoint is working
- Check browser console for error messages

### API Connection Issues
- Ensure the backend is running on `http://localhost:8080`
- Check network tab in browser dev tools for failed requests
- Verify API endpoints match the backend routes

## License

This project is part of the Event Registration System.

