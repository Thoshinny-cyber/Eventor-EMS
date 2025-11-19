import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { orderService } from '../services/orderService';
import { toast } from 'react-toastify';

const Orders = () => {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.id) {
      loadOrders();
    }
  }, [user]);

  const loadOrders = async () => {
    try {
      const data = await orderService.getUserOrders(user.id);
      setOrders(data);
    } catch (error) {
      toast.error('Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteOrder = async (orderId) => {
    if (!window.confirm('Are you sure you want to cancel this order?')) {
      return;
    }

    try {
      await orderService.deleteOrder(orderId);
      toast.success('Order cancelled successfully');
      loadOrders();
    } catch (error) {
      toast.error('Failed to cancel order');
    }
  };

  if (loading) {
    return <div className="loading">Loading orders...</div>;
  }

  return (
    <div className="orders-container">
      <h1>My Orders</h1>

      {orders.length === 0 ? (
        <div className="no-orders">
          <p>You have no orders yet.</p>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map((order) => (
            <div key={order.id} className="order-card">
              <div className="order-header">
                <h3>{order.eventName}</h3>
                <span className="order-date">
                  {new Date(order.date).toLocaleDateString()}
                </span>
              </div>
              <div className="order-details">
                <p><strong>Order ID:</strong> #{order.id}</p>
                <p><strong>Number of Tickets:</strong> {order.count}</p>
                {order.bookedSeatsString && order.bookedSeatsString.length > 0 && (
                  <p><strong>Seats:</strong> {order.bookedSeatsString.join(', ')}</p>
                )}
                <p><strong>Total Price:</strong> S${order.totalPrice}</p>
              </div>
              <div className="order-actions">
                <button
                  className="btn-danger"
                  onClick={() => handleDeleteOrder(order.id)}
                >
                  Cancel Order
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Orders;

