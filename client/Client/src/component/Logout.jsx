import React from 'react';
import axios from 'axios';

/**
 * A reusable component for a Logout button.
 * It handles clearing the user's cookie and redirecting them to the login page.
 *
 * @returns {JSX.Element} The LogoutButton component.
 */
const LogoutButton = () => {

  /**
   * Handles logging out the user by clearing the session cookie and redirecting.
   */
  const handleLogout = async () => {
    try {
      // Optional: Make an API call to your backend to invalidate the session.
      // Uncomment this line if you have a logout endpoint.
      // await axios.post('/v0/auth/logout');

      // Clear the cookie by setting its expiration date to a past date.
      // This is the key step for client-side logout.
      document.cookie = 'token=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT;';

      // Redirect the user to the login or home page.
      window.location.href = '/login';
    } catch (error) {
      console.error('Logout failed:', error);
      // In case of an API error, still clear the client-side cookie and redirect.
      document.cookie = 'token=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT;';
      window.location.href = '/login';
    }
  };

  return (
    <button
      onClick={handleLogout}
      className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
    >
      Logout
    </button>
  );
};

export default LogoutButton;