import api from '../utils/api';

// Note: You'll need to implement wishlist endpoints in the backend
// For now, using localStorage as a temporary solution
export const wishlistService = {
  getWishlist: () => {
    const wishlist = localStorage.getItem('wishlist');
    return wishlist ? JSON.parse(wishlist) : [];
  },

  addToWishlist: (eventId) => {
    const wishlist = wishlistService.getWishlist();
    if (!wishlist.includes(eventId)) {
      wishlist.push(eventId);
      localStorage.setItem('wishlist', JSON.stringify(wishlist));
    }
  },

  removeFromWishlist: (eventId) => {
    const wishlist = wishlistService.getWishlist();
    const updated = wishlist.filter((id) => id !== eventId);
    localStorage.setItem('wishlist', JSON.stringify(updated));
  },

  isInWishlist: (eventId) => {
    const wishlist = wishlistService.getWishlist();
    return wishlist.includes(eventId);
  },

  clearWishlist: () => {
    localStorage.removeItem('wishlist');
  },
};

