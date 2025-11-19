import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const { isAuthenticated, user, logout, isAdmin, isUser } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/home" className="navbar-logo">
          Eventor
        </Link>
        <ul className="navbar-menu">
          <li>
            <Link to="/home">Home</Link>
          </li>
          <li>
            <Link to="/events">Events</Link>
          </li>
          {isAuthenticated ? (
            <>
              {isAdmin && (
                <li>
                  <Link to="/admin">Admin Dashboard</Link>
                </li>
              )}
              {isUser && (
                <>
                  <li>
                    <Link to="/wishlist">Wishlist</Link>
                  </li>
                  <li>
                    <Link to="/my-events">My Events</Link>
                  </li>
                </>
              )}
              <li>
                <Link to="/profile">Profile</Link>
              </li>
              <li className="navbar-user">
                <span>Welcome, {user?.name || user?.username}</span>
              </li>
              <li>
                <button onClick={handleLogout} className="btn-logout">
                  Logout
                </button>
              </li>
            </>
          ) : (
            <>
              <li>
                <Link to="/login">Login</Link>
              </li>
              <li>
                <Link to="/register">Register</Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;

